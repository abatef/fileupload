package com.snd.fileupload2.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum UploadStatus {
    SUCCESS, FAILED, UNKNOWN, NOT_FOUND
}
