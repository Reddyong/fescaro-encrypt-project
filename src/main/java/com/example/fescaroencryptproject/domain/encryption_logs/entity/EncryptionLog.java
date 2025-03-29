package com.example.fescaroencryptproject.domain.encryption_logs.entity;

import com.example.fescaroencryptproject.common.entity.BaseEntity;
import com.example.fescaroencryptproject.common.enums.Operation;
import com.example.fescaroencryptproject.common.enums.Status;
import com.example.fescaroencryptproject.domain.files.entity.File;
import com.example.fescaroencryptproject.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "encryption_logs")
@NoArgsConstructor
@AllArgsConstructor
public class EncryptionLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "operation")
    @Enumerated(value = EnumType.STRING)
    private Operation operation;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

}
