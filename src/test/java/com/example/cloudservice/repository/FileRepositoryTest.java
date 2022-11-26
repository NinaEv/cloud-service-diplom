package com.example.cloudservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.cloudservice.model.File;
import com.example.cloudservice.model.UserDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileRepositoryTest {

    public static final String USERNAME_1 = "Username1";
    public static final String PASSWORD_1 = "Password1";

    public static final UserDao USER_1 = new UserDao(USERNAME_1, PASSWORD_1, null);
    public static final String FILENAME_1 = "Filename1";
    public static final Long SIZE_1 = 100L;
    public static final byte[] FILE_CONTENT_1 = FILENAME_1.getBytes();
    public static final File FILE_1 = new File(FILENAME_1, LocalDateTime.now(), SIZE_1, FILE_CONTENT_1, USER_1);
    public static final String EDIT_FILENAME = "OldFilename";
    public static final Long EDIT_SIZE = 100L;
    public static final byte[] EDIT_FILE_CONTENT = EDIT_FILENAME.getBytes();
    public static final File EDIT_FILE = new File(EDIT_FILENAME, LocalDateTime.now(), EDIT_SIZE, EDIT_FILE_CONTENT, USER_1);
    public static final String NEW_FILENAME = "NewFilename";

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
        userRepository.save(USER_1);
        fileRepository.save(FILE_1);

        fileRepository.save(EDIT_FILE);
    }

    @Test
    void deleteByUserAndFilename() {
        Optional<File> beforeDelete = fileRepository.findById(FILENAME_1);
        assertTrue(beforeDelete.isPresent());
        fileRepository.deleteByUserAndFilename(USER_1, FILENAME_1);
        Optional<File> afterDelete = fileRepository.findById(FILENAME_1);
        assertFalse(afterDelete.isPresent());
    }

    @Test
    void findByUserAndFilename() {
        assertEquals(FILE_1, fileRepository.findByUserAndFilename(USER_1, FILENAME_1));
    }

    @Test
    void editFileNameByUser() {
        Optional<File> beforeEditNameUser = fileRepository.findById(EDIT_FILENAME);
        assertTrue(beforeEditNameUser.isPresent());
        assertEquals(EDIT_FILENAME, beforeEditNameUser.get().getFilename());
        fileRepository.editFileNameByUser(USER_1, EDIT_FILENAME, NEW_FILENAME);
        Optional<File> afterEditName = fileRepository.findById(NEW_FILENAME);
        assertTrue(afterEditName.isPresent());
        assertEquals(NEW_FILENAME, afterEditName.get().getFilename());
    }

    @Test
    void findAllByUser() {
        assertEquals(List.of(FILE_1, EDIT_FILE), fileRepository.findAllByUser(USER_1));
    }
}
