package com.example.fescaroencryptproject.domain.files.controller;

import com.example.fescaroencryptproject.domain.files.dto.FileDTO;
import com.example.fescaroencryptproject.domain.files.dto.response.FileDownloadResponse;
import com.example.fescaroencryptproject.domain.files.service.FileService;
import com.example.fescaroencryptproject.domain.users.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@DisplayName(value = "파일 컨트롤러 테스트")
public class FileControllerTest {

    @MockitoBean
    private FileService fileService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName(value = "바이너리 파일을 업로드 컨트롤러 테스트")
    void uploadTest() throws Exception {
        // given
        byte[] binaryData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}; // 예제 바이너리 데이터
        MockMultipartFile mockFile = new MockMultipartFile("uploadFile", "test.bin", MediaType.APPLICATION_OCTET_STREAM_VALUE, binaryData);
        UserDTO mockUser = UserDTO.of(1L, "test", "test@email.com", "password", "USER", LocalDateTime.now());

        given(fileService.upload(any())).willReturn(FileDTO.of(
                1L,
                mockUser,
                "enc_test.bin",
                "test_path",
                "test_iv",
                LocalDateTime.now()
        ));

        // when
        mockMvc.perform(multipart("/api/v1/files/upload")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.email").value(mockUser.email()))
                .andExpect(jsonPath("$.data.fileName").value("enc_test.bin"))
                .andExpect(jsonPath("$.data.filePath").value("test_path"))
                .andDo(print());

        // then
        verify(fileService).upload(any(MultipartFile.class));
    }

    @Test
    @DisplayName(value = "암호화 된 파일 다운로드 컨트롤러 테스트")
    void downloadEncryptedTest() throws Exception {
        // given
        FileDownloadResponse fileDownloadResponse = FileDownloadResponse.of(
                "enc_test.bin",
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}
        );
        given(fileService.downloadEncrypted(1L)).willReturn(fileDownloadResponse);

        // when
        mockMvc.perform(get("/api/v1/files/{file-id}/download-encrypted", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=enc_test.bin"))
                .andExpect(content().bytes(fileDownloadResponse.downloaded()))
                .andDo(print());

        // then
        verify(fileService).downloadEncrypted(1L);
    }

    @Test
    @DisplayName(value = "암호화 대상 파일 다운로드 컨트롤러 테스트")
    void downloadTest() throws Exception {
        // given
        FileDownloadResponse downloadResponse = FileDownloadResponse.of(
                "test.bin",
                new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}
        );
        given(fileService.download(1L)).willReturn(downloadResponse);

        // when
        mockMvc.perform(get("/api/v1/files/{file-id}/download", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.bin"))
                .andExpect(content().bytes(downloadResponse.downloaded()))
                .andDo(print());

        // then
        verify(fileService).download(1L);

    }

    @Test
    @DisplayName(value = "암호화 현황 리스트 조회 컨트롤러 테스트")
    void findAllTest() throws Exception {
        // given
        UserDTO mockUser = UserDTO.of(1L, "test", "test@email.com", "password", "USER", LocalDateTime.now());
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        FileDTO fileDTO = FileDTO.of(1L, mockUser, "enc_test.bin", "test_path", "test_iv", LocalDateTime.now());
        Page<FileDTO> pageDTO = new PageImpl<>(Collections.singletonList(fileDTO), pageable, 1);

        given(fileService.findAll(pageable)).willReturn(pageDTO);

        // when
        mockMvc.perform(get("/api/v1/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].decryptedFileName").value("test.bin"))
                .andExpect(jsonPath("$.data.content[0].encryptedFileName").value("enc_test.bin"))
                .andExpect(jsonPath("$.data.content[0].iv").value("test_iv"))
                .andDo(print());

        // then
        verify(fileService).findAll(pageable);

    }
}
