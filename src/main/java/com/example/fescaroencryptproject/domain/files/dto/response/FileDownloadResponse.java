package com.example.fescaroencryptproject.domain.files.dto.response;

public record FileDownloadResponse(
        String fileName,
        byte[] downloaded
) {
    public static FileDownloadResponse of(String fileName, byte[] downloaded) {
        return new FileDownloadResponse(fileName, downloaded);
    }
}
