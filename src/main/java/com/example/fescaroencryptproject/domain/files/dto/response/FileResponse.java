package com.example.fescaroencryptproject.domain.files.dto.response;

import com.example.fescaroencryptproject.domain.files.dto.FileDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FileResponse(
        Long id,
        String decryptedFileName,
        String encryptedFileName,
        String iv,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static FileResponse of(Long id, String decryptedFileName, String encryptedFileName, String iv, LocalDateTime createdAt) {
        return new FileResponse(id, decryptedFileName, encryptedFileName, iv, createdAt);
    }
    public static FileResponse from(FileDTO fileDTO) {
        return FileResponse.of(
                fileDTO.id(),
                fileDTO.fileName().substring(4),
                fileDTO.fileName(),
                fileDTO.initializationVector(),
                fileDTO.createdAt()
        );
    }
}
