package com.example.cloudservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.cloudservice.model.EditFileNameRequest;
import com.example.cloudservice.model.FileResponse;
import com.example.cloudservice.service.FileService;

import java.util.List;

@RestController
@AllArgsConstructor
public class FileController {

    private FileService fileService;


    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFiles(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, MultipartFile file) {
        fileService.uploadFile(authToken, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        final byte[] file = fileService.downloadFile(authToken, filename);
        return ResponseEntity.ok().body(new ByteArrayResource(file));
    }

    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public ResponseEntity<?> editFileName(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody EditFileNameRequest changeFileNameRequest) {
        fileService.editFileName(authToken, filename, changeFileNameRequest);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FileResponse> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") Integer limit) {
        return fileService.getAllFiles(authToken, limit);
    }


}
