package com.snd.fileupload2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponse {
    private Integer fileId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String message;
    private UploadStatus status;
}
