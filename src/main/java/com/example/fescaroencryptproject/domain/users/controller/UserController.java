package com.example.fescaroencryptproject.domain.users.controller;

import com.example.fescaroencryptproject.common.entity.ApiResponse;
import com.example.fescaroencryptproject.domain.users.dto.request.UserRequest;
import com.example.fescaroencryptproject.domain.users.entity.User;
import com.example.fescaroencryptproject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserRepository userRepository;

    // 사용할 회원 정보 생성 위한 임시 기능
    @PostMapping(path = "/save")
    public ResponseEntity<ApiResponse<?>> save(
            @RequestBody UserRequest userRequest
    ) {
        userRepository.save(new User(userRequest.username(), userRequest.email(), userRequest.password(), userRequest.role()));

        return ResponseEntity.ok(ApiResponse.ok());
    }
}
