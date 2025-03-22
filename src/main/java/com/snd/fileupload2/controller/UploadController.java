package com.snd.fileupload2.controller;

import com.snd.fileupload2.dto.FileUploadResponse;
import com.snd.fileupload2.dto.UploadStatus;
import com.snd.fileupload2.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class UploadController {
    private final StorageService storageService;

    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadResponse fileUploadResponse = storageService.upload(file);
        if (fileUploadResponse.getStatus() == UploadStatus.SUCCESS) {
            return ResponseEntity.ok(fileUploadResponse);
        }
        return new ResponseEntity<>(fileUploadResponse, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<FileUploadResponse> downloadFile(@RequestParam Integer fileId) throws IOException {
        FileUploadResponse fileUploadResponse = storageService.getFileById(fileId);
        if (fileUploadResponse.getStatus() == UploadStatus.SUCCESS) {
            return ResponseEntity.ok(fileUploadResponse);
        }
        return new ResponseEntity<>(fileUploadResponse, HttpStatus.NOT_FOUND);
    }
}
