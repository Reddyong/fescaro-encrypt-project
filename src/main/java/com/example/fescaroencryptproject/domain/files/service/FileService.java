package com.example.fescaroencryptproject.domain.files.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.fescaroencryptproject.common.enums.Operation;
import com.example.fescaroencryptproject.common.enums.Status;
import com.example.fescaroencryptproject.common.util.AESUtil;
import com.example.fescaroencryptproject.domain.encryption_keys.entity.EncryptionKey;
import com.example.fescaroencryptproject.domain.encryption_keys.repository.EncryptionKeyRepositoryPort;
import com.example.fescaroencryptproject.domain.encryption_logs.entity.EncryptionLog;
import com.example.fescaroencryptproject.domain.encryption_logs.repository.EncryptionLogRepositoryPort;
import com.example.fescaroencryptproject.domain.files.dto.FileDTO;
import com.example.fescaroencryptproject.domain.files.dto.response.FileDownloadResponse;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3 amazonS3;
    private final FileRepositoryPort fileRepository;
    private final UserRepositoryPort userRepository;
    private final EncryptionKeyRepositoryPort encryptionKeyRepository;
    private final EncryptionLogRepositoryPort encryptionLogRepository;

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

        String encryptedFileName = "enc_" + uploadFile.getOriginalFilename();

        // S3에 업로드
        String fileURL = putS3(encrypted, encryptedFileName, ivBase64);
        log.info("en : " + Arrays.toString(encrypted));

        // 파일 db에 저장
        User user = userRepository.findById(1L);    // 해당 프로젝트에서는 회원 정보 임시 조회

        File file = File.of(user, encryptedFileName, fileURL, ivBase64);
        File savedFile = fileRepository.save(file);

        // 암호화 키 정보 db에 저장.
        EncryptionKey encryptionKey = EncryptionKey.of(user, savedFile, secretKeyBase64);
        encryptionKeyRepository.save(encryptionKey);

        // 암호화 로그 db에 저장.
        EncryptionLog encryptionLog = EncryptionLog.of(user, savedFile, Operation.ENCRYPT, Status.SUCCESS);
        encryptionLogRepository.save(encryptionLog);

        return FileDTO.from(savedFile);
    }

    public FileDownloadResponse downloadEncrypted(Long fileId) {
        // 파일 정보 db 에서 조회
        File file = fileRepository.findById(fileId);

        // 해당 파일의 이름과 iv 값 추출
        String fileName = file.getFileName();
        String iv = file.getInitializationVector();

        // 해당하는 암호화 파일 S3에서 조회 후 반환
        byte[] downloaded = getS3(fileName, iv);

        return FileDownloadResponse.of(fileName, downloaded);
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

    private byte[] getS3(String fileName, String iv) {
        S3Object s3Object = amazonS3.getObject(bucket, DIR_NAME + "/" + fileName + "_" + iv);
        log.info("object : " + s3Object);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] byteArray = outputStream.toByteArray();
            log.info("dw : " + Arrays.toString(byteArray));

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
