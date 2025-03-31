package com.example.fescaroencryptproject.domain.encryption_keys.repository;

import com.example.fescaroencryptproject.domain.encryption_keys.entity.EncryptionKey;

public interface EncryptionKeyRepositoryPort {
    EncryptionKey save(EncryptionKey encryptionKey);
}
