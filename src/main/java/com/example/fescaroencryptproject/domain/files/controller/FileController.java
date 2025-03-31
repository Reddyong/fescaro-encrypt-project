package com.example.fescaroencryptproject.domain.files.controller;

import com.example.fescaroencryptproject.common.entity.ApiResponse;
import com.example.fescaroencryptproject.domain.files.dto.response.FileEncryptResponse;
import com.example.fescaroencryptproject.domain.files.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> upload(
            @RequestPart MultipartFile uploadFile
    ) throws Exception {

        return ResponseEntity.ok(
                ApiResponse.ok(FileEncryptResponse.from(fileService.upload(uploadFile)))
        );
    }
}
