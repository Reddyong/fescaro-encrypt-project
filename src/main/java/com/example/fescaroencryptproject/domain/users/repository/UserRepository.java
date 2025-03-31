package com.example.fescaroencryptproject.domain.users.repository;

import com.example.fescaroencryptproject.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
