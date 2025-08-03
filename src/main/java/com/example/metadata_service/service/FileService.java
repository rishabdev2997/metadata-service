package com.example.metadata_service.service;

import com.example.metadata_service.entity.FileEntity;
import com.example.metadata_service.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity save(FileEntity file) {
        return fileRepository.save(file);
    }

    public Optional<FileEntity> findByShortLink(String shortLink) {
        return fileRepository.findByShortLinkAndIsDeletedFalse(shortLink);
    }

    public List<String> findExpiredOrMaxedOutShortLinks(Instant now) {
        List<String> shortLinks = fileRepository.findExpiredOrMaxedOutShortLinks(now);
        logger.info("Expired or maxed-out shortLinks fetched: {}", shortLinks);
        return shortLinks;
    }

    public void deleteById(UUID id) {
        fileRepository.deleteById(id);
    }

    public boolean incrementDownloadCount(String shortLink) {
        Optional<FileEntity> fileOpt = findByShortLink(shortLink);
        if (fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();
            Integer count = file.getDownloadCount();
            file.setDownloadCount((count == null) ? 1 : count + 1);
            save(file);
            return true;
        }
        return false;
    }
}
