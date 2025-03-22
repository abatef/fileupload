package com.snd.fileupload2.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.snd.fileupload2.dto.FileUploadResponse;
import com.snd.fileupload2.dto.UploadStatus;
import com.snd.fileupload2.models.File;
import com.snd.fileupload2.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AzureStorageService implements StorageService {

    @Value("${azure.storage.connection-string}")
    private String AZURE_CONN_STR;

    @Value("${azure.storage.container-name}")
    private String AZURE_CONT_NAME;


    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient blobContainerClient;
    private final FileRepository fileRepository;

    @Autowired
    public AzureStorageService(FileRepository fileRepository) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(AZURE_CONN_STR)
                .buildClient();
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(AZURE_CONT_NAME);
        this.fileRepository = fileRepository;;
    }

    @Transactional
    @Override
    public FileUploadResponse upload(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            String fileExtension = "";
            if (fileName != null && fileName.contains(".")) {
                fileExtension = fileName.substring(fileName.lastIndexOf("."));
            }
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String uniqueFileName = timestamp + '_' + fileExtension;
            BlobClient blobClient = blobContainerClient.getBlobClient(uniqueFileName);
            try (InputStream data = file.getInputStream()) {
                blobClient.upload(data, file.getSize());
            }

            File newFile = File.builder()
                    .name(fileName)
                    .url(blobClient.getBlobUrl())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .build();

            File savedFile = fileRepository.save(newFile);


            return FileUploadResponse.builder()
                    .fileId(savedFile.getId())
                    .fileName(file.getOriginalFilename())
                    .fileUrl(blobClient.getBlobUrl())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .message("Successfully uploaded " + file.getOriginalFilename())
                    .status(UploadStatus.SUCCESS)
                    .build();
        } catch (Exception e) {
            return FileUploadResponse.builder()
                    .message("Failed to upload file.")
                    .status(UploadStatus.FAILED)
                    .build();
        }
    }

    @Override
    public FileUploadResponse getFileById(Integer id) {
        File file = fileRepository.findById(id).orElse(null);
        if (file == null) {
            return FileUploadResponse.builder()
                    .status(UploadStatus.NOT_FOUND)
                    .message("file: " + id + " doesn't exist, make sure the id is correct")
                    .build();
        }
        return FileUploadResponse.builder()
                .fileId(id)
                .fileName(file.getName())
                .fileUrl(file.getUrl())
                .fileType(file.getType())
                .fileSize(file.getSize())
                .message("File Found Successfully")
                .status(UploadStatus.SUCCESS)
                .build();
    }
}
