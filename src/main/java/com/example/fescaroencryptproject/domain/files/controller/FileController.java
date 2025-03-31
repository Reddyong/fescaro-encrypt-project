package com.example.fescaroencryptproject.domain.files.controller;

import com.example.fescaroencryptproject.common.entity.ApiResponse;
import com.example.fescaroencryptproject.domain.files.dto.response.FileDownloadResponse;
import com.example.fescaroencryptproject.domain.files.dto.response.FileEncryptResponse;
import com.example.fescaroencryptproject.domain.files.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(path = "/{file-id}/download-encrypted")
    public ResponseEntity<?> downloadEncrypted(
            @PathVariable(name = "file-id") Long fileId
    ) {
        FileDownloadResponse fileDownloadResponse = fileService.downloadEncrypted(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDownloadResponse.fileName())
                .body(fileDownloadResponse.downloaded());
    }

}
