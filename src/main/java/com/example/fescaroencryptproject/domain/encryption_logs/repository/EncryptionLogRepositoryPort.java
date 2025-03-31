package com.example.fescaroencryptproject.domain.encryption_logs.repository;

import com.example.fescaroencryptproject.domain.encryption_logs.entity.EncryptionLog;

public interface EncryptionLogRepositoryPort {
    EncryptionLog save(EncryptionLog encryptionLog);
}
