package com.example.fescaroencryptproject.domain.users.dto;

import com.example.fescaroencryptproject.domain.users.entity.User;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String username,
        String email,
        String password,
        String role,
        LocalDateTime createdAt
) {
    public static UserDTO of(Long id, String username, String email, String password, String role, LocalDateTime createdAt) {
        return new UserDTO(id, username, email, password, role, createdAt);
    }

    public static UserDTO from(User user) {
        return UserDTO.of(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
