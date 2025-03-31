package com.example.fescaroencryptproject.domain.users.repository;

import com.example.fescaroencryptproject.domain.users.entity.User;

public interface UserRepositoryPort {
    User findById(Long id);
}
