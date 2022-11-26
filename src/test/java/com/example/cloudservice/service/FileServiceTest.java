package com.example.cloudservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.example.cloudservice.exception.InputDataException;
import com.example.cloudservice.exception.UnauthorizedException;
import com.example.cloudservice.model.EditFileNameRequest;
import com.example.cloudservice.model.File;
import com.example.cloudservice.model.FileResponse;
import com.example.cloudservice.model.UserDao;
import com.example.cloudservice.repository.FileRepository;
import com.example.cloudservice.repository.JwtAuthenticationRepository;
import com.example.cloudservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.cloudservice.service.JwtAuthenticationServiceTest.BEARER_TOKEN;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FileServiceTest {

    public static final String TOKEN_1 = "Token1";
    public static final String USERNAME_1 = "Username1";
    public static final String PASSWORD_1 = "Password1";
    public static final UserDao USER_1 = new UserDao(USERNAME_1, PASSWORD_1, null);
    public static final String USERNAME_2 = "Username1";
    public static final String PASSWORD_2 = "Password1";
    public static final UserDao USER_2 = new UserDao(USERNAME_2, PASSWORD_2, null);
    public static final String BEARER_TOKEN_SPLIT = BEARER_TOKEN.split(" ")[1];
    public static final String FILENAME_1 = "Filename1";
    public static final String FILENAME_2 = "Filename2";
    public static final byte[] FILE_CONTENT_1 = FILENAME_1.getBytes();
    public static final byte[] FILE_CONTENT_2 = FILENAME_2.getBytes();
    public static final MultipartFile MULTIPART_FILE = new MockMultipartFile(FILENAME_1, FILE_CONTENT_1);
    public static final Long SIZE_1 = 100L;
    public static final File FILE_1 = new File(FILENAME_1, LocalDateTime.now(), SIZE_1, FILE_CONTENT_1, USER_1);
    public static final Long SIZE_2 = 100L;
    public static final File FILE_2 = new File(FILENAME_2, LocalDateTime.now(), SIZE_2, FILE_CONTENT_2, USER_2);
    public static final Integer LIMIT = 100;
    public static final String NEW_FILENAME = "NewFilename";
    public static final EditFileNameRequest EDIT_FILE_NAME_REQUEST = new EditFileNameRequest(NEW_FILENAME);
    public static final List<File> FILE_LIST = List.of(FILE_1, FILE_2);
    public static final FileResponse FILE_RESPONSE_1 = new FileResponse(FILENAME_1, SIZE_1);
    public static final FileResponse FILE_RESPONSE_2 = new FileResponse(FILENAME_2, SIZE_2);
    public static final List<FileResponse> FILE_RESPONSE_LIST = List.of(FILE_RESPONSE_1, FILE_RESPONSE_2);

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private JwtAuthenticationRepository jwtAuthenticationRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(jwtAuthenticationRepository.getUsernameByToken(BEARER_TOKEN_SPLIT)).thenReturn(USERNAME_1);
        Mockito.when(userRepository.findByUsername(USERNAME_1)).thenReturn(USER_1);
    }

    @Test
    void uploadFile() {
        assertTrue(fileService.uploadFile(BEARER_TOKEN, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void uploadFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.uploadFile(TOKEN_1, FILENAME_1, MULTIPART_FILE));
    }

    @Test
    void deleteFile() {
        fileService.deleteFile(BEARER_TOKEN, FILENAME_1);
        Mockito.verify(fileRepository, Mockito.times(1)).deleteByUserAndFilename(USER_1, FILENAME_1);
    }

    @Test
    void deleteFileUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.deleteFile(TOKEN_1, FILENAME_1));
    }

    @Test
    void deleteFileInputDataException() {
        Mockito.when(fileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(FILE_1);
        assertThrows(InputDataException.class, () -> fileService.deleteFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFile() {
        Mockito.when(fileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(FILE_1);
        assertEquals(FILE_CONTENT_1, fileService.downloadFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void downloadFileUnauthorizedException() {
        Mockito.when(fileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(FILE_1);
        assertThrows(UnauthorizedException.class, () -> fileService.downloadFile(TOKEN_1, FILENAME_1));
    }

    @Test
    void downloadFileInputDataException() {
        Mockito.when(fileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(FILE_1);
        assertThrows(InputDataException.class, () -> fileService.downloadFile(BEARER_TOKEN, FILENAME_2));
    }

    @Test
    void editFileName() {
        fileService.editFileName(BEARER_TOKEN, FILENAME_1, EDIT_FILE_NAME_REQUEST);
        Mockito.verify(fileRepository, Mockito.times(1)).editFileNameByUser(USER_1, FILENAME_1, NEW_FILENAME);
    }

    @Test
    void editFileNameUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> fileService.editFileName(TOKEN_1, FILENAME_1, EDIT_FILE_NAME_REQUEST));
    }

    @Test
    void editFileNameInputDataException() {
        Mockito.when(fileRepository.findByUserAndFilename(USER_1, FILENAME_1)).thenReturn(FILE_1);
        assertThrows(InputDataException.class, () -> fileService.deleteFile(BEARER_TOKEN, FILENAME_1));
    }

    @Test
    void getAllFiles() {
        Mockito.when(fileRepository.findAllByUser(USER_1)).thenReturn(FILE_LIST);
        assertEquals(FILE_RESPONSE_LIST, fileService.getAllFiles(BEARER_TOKEN, LIMIT));
    }

    @Test
    void getAllFilesUnauthorizedException() {
        Mockito.when(fileRepository.findAllByUser(USER_1)).thenReturn(FILE_LIST);
        assertThrows(UnauthorizedException.class, () -> fileService.getAllFiles(TOKEN_1, LIMIT));
    }
}
