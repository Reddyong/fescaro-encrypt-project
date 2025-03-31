package com.example.fescaroencryptproject.domain.files.dto;

import com.example.fescaroencryptproject.domain.files.entity.File;
import com.example.fescaroencryptproject.domain.users.dto.UserDTO;

import java.time.LocalDateTime;

public record FileDTO(
        Long id,
        UserDTO userDTO,
        String fileName,
        String filePath,
        String initializationVector,
        LocalDateTime createdAt
) {
    public static FileDTO of(Long id, UserDTO userDTO, String fileName, String filePath, String initializationVector, LocalDateTime createdAt) {
        return new FileDTO(id, userDTO, fileName, filePath, initializationVector, createdAt);
    }

    public static FileDTO from(File file) {
        return FileDTO.of(
                file.getId(),
                UserDTO.from(file.getUser()),
                file.getFileName(),
                file.getFilePath(),
                file.getInitializationVector(),
                file.getCreatedAt()
        );
    }
}
