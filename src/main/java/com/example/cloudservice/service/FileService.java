package com.example.cloudservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.cloudservice.model.*;
import com.example.cloudservice.exception.*;
import com.example.cloudservice.repository.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {

    private FileRepository fileRepository;
    private JwtAuthenticationRepository jwtAuthenticationRepository;
    private UserRepository userRepository;


    @Transactional
    public void deleteFile(String authToken, String filename) {
        final UserDao userDao = getUserByAuthToken(authToken);
        if (userDao == null) {
            System.out.println("Unauthorized error");
            throw new UnauthorizedException("Unauthorized error");
        }
        fileRepository.deleteByUserAndFilename(userDao, filename);
        final File getListDeleteFiles = fileRepository.findByUserAndFilename(userDao, filename);
        if (getListDeleteFiles != null) {
            System.out.println("Error input data");
            throw new InputDataException("Error input data");
        }
        System.out.println("The file was deleted successfully. User: " + userDao.getUsername());
    }

    public boolean uploadFile(String authToken, String filename, MultipartFile file) {
        final UserDao userDao = getUserByAuthToken(authToken);
        if (userDao == null) {
            System.out.println("Unauthorized error");
            throw new UnauthorizedException("Unauthorized error");
        }

        try {
            fileRepository.save(new File(filename, LocalDateTime.now(), file.getSize(), file.getBytes(), userDao));
            System.out.println("File uploaded successfully. User: " + userDao.getUsername());
            return true;
        } catch (IOException e) {
            System.out.println("Error input data");
            throw new InputDataException("Error input data");
        }
    }

    public byte[] downloadFile(String authToken, String filename) {
        final UserDao userDao = getUserByAuthToken(authToken);
        if (userDao == null) {
            System.out.println("Unauthorized error");
            throw new UnauthorizedException("Unauthorized error");
        }
        final File file = fileRepository.findByUserAndFilename(userDao, filename);
        if (file == null) {
            System.out.println("Error input data");
            throw new InputDataException("Error input data");
        }
        System.out.println("Success download file from cloud. User: " + userDao.getUsername());
        return file.getFileContents();
    }

    @Transactional
    public void editFileName(String authToken, String filename, EditFileNameRequest editFileNameRequest) {
        final UserDao userDao = getUserByAuthToken(authToken);
        if (userDao == null) {
            System.out.println("Unauthorized error");
            throw new UnauthorizedException("Unauthorized error");
        }
        fileRepository.editFileNameByUser(userDao, filename, editFileNameRequest.getFilename());
        final File oldFilename = fileRepository.findByUserAndFilename(userDao, filename);
        if (oldFilename != null) {
            System.out.println("Error input data");
            throw new InputDataException("Error input data");
        }
        System.out.println("File success upload. User: " + userDao.getUsername());
    }

    public List<FileResponse> getAllFiles(String authToken, Integer limit) {
        final UserDao userDao = getUserByAuthToken(authToken);
        if (userDao == null) {
            System.out.println("Unauthorized error");
            throw new UnauthorizedException("Unauthorized error");
        }
        System.out.println("Success get list. User: " + userDao.getUsername());
        return fileRepository.findAllByUser(userDao).stream()
                .map(p -> new FileResponse(p.getFilename(), p.getSize()))
                .collect(Collectors.toList());
    }

    private UserDao getUserByAuthToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            final String authTokenBearer = authToken.split(" ")[1];
            final String username = jwtAuthenticationRepository.getUsernameByToken(authTokenBearer);
            return userRepository.findByUsername(username);
        }
        return null;
    }
}
