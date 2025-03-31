package com.example.fescaroencryptproject.domain.files.entity;

import com.example.fescaroencryptproject.common.entity.BaseEntity;
import com.example.fescaroencryptproject.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "initialization_vector")
    private String initializationVector;

    public static File of(User user, String fileName, String filePath, String initializationVector) {
        return new File(user, fileName, filePath, initializationVector);
    }

}
