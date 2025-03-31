package com.example.fescaroencryptproject.domain.files.dto.response;

import com.example.fescaroencryptproject.domain.files.dto.FileDTO;

public record FileEncryptResponse(
        Long id,
        String email,
        String fileName,
        String filePath
) {
    public static FileEncryptResponse of(Long id, String email, String fileName, String filePath) {
        return new FileEncryptResponse(id, email, fileName, filePath);
    }

    public static FileEncryptResponse from(FileDTO fileDTO) {
        return FileEncryptResponse.of(
                fileDTO.id(),
                fileDTO.userDTO().email(),
                fileDTO.fileName(),
                fileDTO.filePath()
        );
    }
}
