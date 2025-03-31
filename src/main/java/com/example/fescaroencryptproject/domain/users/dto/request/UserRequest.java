package com.example.fescaroencryptproject.domain.users.dto.request;

public record UserRequest(
        String username,
        String email,
        String password,
        String role
) {
}
