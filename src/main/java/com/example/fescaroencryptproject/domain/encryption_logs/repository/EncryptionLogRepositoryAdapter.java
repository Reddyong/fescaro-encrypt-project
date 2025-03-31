package com.example.fescaroencryptproject.domain.encryption_logs.repository;

import com.example.fescaroencryptproject.domain.encryption_logs.entity.EncryptionLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EncryptionLogRepositoryAdapter implements EncryptionLogRepositoryPort {
    private final EncryptionLogRepository encryptionLogRepository;

    @Override
    public EncryptionLog save(EncryptionLog encryptionLog) {
        return encryptionLogRepository.save(encryptionLog);
    }

}
