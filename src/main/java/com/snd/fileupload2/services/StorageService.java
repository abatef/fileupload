package com.snd.fileupload2.services;

import com.snd.fileupload2.dto.FileUploadResponse;
import com.snd.fileupload2.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {
    FileUploadResponse upload(MultipartFile file) throws IOException;
    FileUploadResponse getFileById(Integer fileId) throws IOException;
}
