package com.example.metadata_service.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

import lombok.Data;

@Data
@Entity
@Table(name = "file_entity")
public class FileEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "short_link", nullable = false, unique = true)
    private String shortLink;

    @Column(name = "bucket_path", nullable = false)
    private String bucketPath;

    @Column(name = "upload_time", nullable = false)
    private Instant uploadTime;

    @Column(name = "expiry_time")
    private Instant expiryTime;

    @Column(name = "max_downloads")
    private Integer maxDownloads;

    @Column(name = "download_count", nullable = false)
    private Integer downloadCount = 0;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
