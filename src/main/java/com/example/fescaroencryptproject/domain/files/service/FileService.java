package com.example.fescaroencryptproject.domain.files.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.fescaroencryptproject.common.util.AESUtil;
import com.example.fescaroencryptproject.domain.files.dto.FileDTO;
import com.example.fescaroencryptproject.domain.files.entity.File;
import com.example.fescaroencryptproject.domain.files.repository.FileRepositoryPort;
import com.example.fescaroencryptproject.domain.users.entity.User;
import com.example.fescaroencryptproject.domain.users.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3 amazonS3;
    private final FileRepositoryPort fileRepository;
    private final UserRepositoryPort userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String DIR_NAME = "fescaro-binary-file";

    @Transactional
    public FileDTO upload(MultipartFile uploadFile) throws Exception {
        // 바이너리 파일 암호화
        byte[] uploadFileBytes = uploadFile.getBytes();

        byte[] iv = AESUtil.generateIV();
        SecretKey secretKey = AESUtil.getSecretKey();

        byte[] encrypted = AESUtil.encrypt(uploadFileBytes, iv, secretKey);

        String ivBase64 = Base64.getEncoder().encodeToString(iv);
        String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        log.info("sk : " + secretKeyBase64);

        String encryptedFileName = "enc_" + uploadFile.getOriginalFilename();

        // S3에 업로드
        String fileURL = putS3(encrypted, encryptedFileName, ivBase64);

        // 파일 db에 저장
        User user = userRepository.findById(1L);    // 해당 프로젝트에서는 회원 정보 임시 조회

        File file = File.of(user, encryptedFileName, fileURL, ivBase64);
        File savedFile = fileRepository.save(file);

        return FileDTO.from(savedFile);
    }

    private String putS3(byte[] file, String fileName, String iv) {
        String uniqueFileName = DIR_NAME + "/" + fileName + "_" + iv;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("iv", iv);
        metadata.setContentLength(file.length);
        amazonS3.putObject(bucket, uniqueFileName, inputStream, metadata);

        return amazonS3.getUrl(bucket, uniqueFileName).toString();
    }
}
