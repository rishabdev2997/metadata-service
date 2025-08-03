package com.example.metadata_service.controller;

import com.example.metadata_service.entity.FileEntity;
import com.example.metadata_service.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/internal")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/metadata")
    public ResponseEntity<FileEntity> createFileMetadata(@RequestBody FileEntity file) {
        FileEntity savedFile = fileService.save(file);
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/metadata/{shortLink}")
    public ResponseEntity<FileEntity> getFileMetadata(@PathVariable String shortLink) {
        Optional<FileEntity> fileOpt = fileService.findByShortLink(shortLink);
        return fileOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/metadata/{shortLink}/increment-download")
    public ResponseEntity<Void> incrementDownloadCount(@PathVariable String shortLink) {
        boolean updated = fileService.incrementDownloadCount(shortLink);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID id) {
        fileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metadata/expired")
    public ResponseEntity<List<String>> getExpiredShortLinks() {
        Instant now = Instant.now();
        List<String> expiredShortLinks = fileService.findExpiredOrMaxedOutShortLinks(now);
        return ResponseEntity.ok(expiredShortLinks);
    }

    // New delete-by-shortlink endpoint
    @DeleteMapping("/files/by-shortlink/{shortLink}")
    public ResponseEntity<Void> deleteByShortLink(@PathVariable String shortLink) {
        Optional<FileEntity> fileOpt = fileService.findByShortLink(shortLink);
        if (fileOpt.isPresent()) {
            fileService.deleteById(fileOpt.get().getId());
            return ResponseEntity.noContent().build();  // 204 No Content
        }
        return ResponseEntity.notFound().build();  // 404 Not Found
    }
}
