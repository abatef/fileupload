package com.snd.fileupload2.exceptions;


public class FileNotFoundException extends RuntimeException {
    private final Integer fileId;
    public FileNotFoundException(String message, Integer fileId) {
        super(message);
        this.fileId = fileId;
    }
}
