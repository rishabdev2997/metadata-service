package com.example.metadata_service.repository;

import com.example.metadata_service.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    Optional<FileEntity> findByShortLinkAndIsDeletedFalse(String shortLink);

    @Query("SELECT f.shortLink FROM FileEntity f " +
            "WHERE f.isDeleted = false " +
            "AND f.shortLink IS NOT NULL AND f.shortLink <> '' " +
            "AND ( " +
            " (f.expiryTime IS NOT NULL AND f.expiryTime < :now) OR " +
            " (f.maxDownloads IS NOT NULL AND f.downloadCount >= f.maxDownloads) " +
            ")")
    List<String> findExpiredOrMaxedOutShortLinks(@Param("now") Instant now);
}
