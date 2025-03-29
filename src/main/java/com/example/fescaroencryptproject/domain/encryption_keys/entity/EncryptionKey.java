package com.example.fescaroencryptproject.domain.encryption_keys.entity;

import com.example.fescaroencryptproject.common.entity.BaseEntity;
import com.example.fescaroencryptproject.domain.files.entity.File;
import com.example.fescaroencryptproject.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "encryption_keys")
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionKey extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "key_reference")
    private String keyReference;
}
