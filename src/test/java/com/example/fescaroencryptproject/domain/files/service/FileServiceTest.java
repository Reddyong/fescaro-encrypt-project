package com.example.fescaroencryptproject.domain.files.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName(value = "파일 서비스 테스트")
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private FileRepositoryPort fileRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private EncryptionKeyRepositoryPort encryptionKeyRepository;

    @Mock
    private EncryptionLogRepositoryPort encryptionLogRepository;

    @Mock
    private MultipartFile uploadFile;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Test
    @DisplayName(value = "파일 업로드 테스트")
    void uploadTest() throws Exception {
        // given
        byte[] fileBytes = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        String fileName = "test.bin";
        String encryptedFileName = "enc_test.bin";
        String ivBase64 = "dummy_iv";
        String fileURL = "https://s3.amazonaws.com/test-bucket/fescaro-binary-file/enc_test.bin_dummy_iv";

        // Setting up mocks
        when(uploadFile.getBytes()).thenReturn(fileBytes);
        when(uploadFile.getOriginalFilename()).thenReturn(fileName);

        // Mocking S3 URL return
        when(amazonS3.getUrl(eq(bucket), anyString())).thenReturn(new java.net.URL(fileURL));

        // Mocking database calls
        File file = File.of(new User(), encryptedFileName, fileURL, ivBase64);
        when(fileRepository.save(any(File.class))).thenReturn(file);
        when(userRepository.findById(1L)).thenReturn(new User());

        // when
        FileDTO fileDTO = fileService.upload(uploadFile);

        // then
        assertNotNull(fileDTO);
        verify(fileRepository).save(any(File.class));
        verify(amazonS3).putObject(eq(bucket), anyString(), any(ByteArrayInputStream.class), any(ObjectMetadata.class));
        verify(amazonS3).getUrl(eq(bucket), anyString());  // S3 URL 생성 호출 검증
        verify(userRepository).findById(1L);
        verify(encryptionKeyRepository).save(any(EncryptionKey.class));
        verify(encryptionLogRepository).save(any(EncryptionLog.class));
    }

    @Test
    @DisplayName(value = "암호화 된 파일 다운로드 서비스 테스트")
    void downloadEncryptedTest() {
        // given
        String fileName = "enc_test.bin";
        String ivBase64 = "dummy_iv";
        String fileURL = "https://s3.amazonaws.com/test-bucket/fescaro-binary-file/enc_test.bin_dummy_iv";

        // Mock S3Object 생성
        S3Object s3Object = new S3Object();
        byte[] dummyData = "test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dummyData);
        S3ObjectInputStream s3InputStream = new S3ObjectInputStream(inputStream, null);
        s3Object.setObjectContent(s3InputStream);

        File file = File.of(new User(), fileName, fileURL, ivBase64);
        when(fileRepository.findById(1L)).thenReturn(file);
        when(amazonS3.getObject(eq(bucket), anyString())).thenReturn(s3Object);

        // when
        FileDownloadResponse response = fileService.downloadEncrypted(1L);

        // then
        assertNotNull(response);
        verify(fileRepository).findById(1L);
        verify(amazonS3).getObject(eq(bucket), anyString());
    }

    @Test
    @DisplayName(value = "암호화 현황 조회 서비스 테스트")
    void findAllTest() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<File> fileList = List.of(
                new File(new User(), "enc_file1.bin", "https://s3.amazonaws.com/bucket/enc_file1.bin", "dummy_iv1"),
                new File(new User(), "enc_file2.bin", "https://s3.amazonaws.com/bucket/enc_file2.bin", "dummy_iv2")
        );

        Page<File> filePage = new PageImpl<>(fileList, pageRequest, fileList.size());

        when(fileRepository.findAll(pageRequest)).thenReturn(filePage);

        // when
        Page<FileDTO> fileDTOS = fileService.findAll(pageRequest);

        // then
        assertNotNull(fileDTOS);
        assertEquals(2, fileDTOS.getTotalElements());
        assertEquals("enc_file1.bin", fileDTOS.getContent().get(0).fileName());
        verify(fileRepository).findAll(pageRequest);
    }
}
