package com.example.jiraproject.file.service;

import com.example.jiraproject.common.exception.JiraFileUploadException;
import com.example.jiraproject.file.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public interface FileService {
    void init();
    void save(String userId, MultipartFile file);
    Resource load(String fileId);
    void deleteAll();
    Stream<Path> loadAll();
}
@Service
@Slf4j
class FileServiceImpl implements FileService {
    public static final Path ROOT = Paths.get("./src/main/resources/uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(ROOT);
            log.info("Created ROOT for file uploads");
        } catch (IOException e) {
           throw new JiraFileUploadException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(String username, MultipartFile file){
        String fileName = username + FileUtil.SUFFIX;
        if (!ROOT.toFile().exists()) { //create ROOT if ROOT doesn't exist
            init();
        }
        try {
            if (ROOT.resolve(fileName).toFile().exists()) {
                log.info("OLD FILE EXISTED");
                Files.delete(ROOT.resolve(fileName));
                log.info("DELETED OLD FILE");
            }
            Files.copy(file.getInputStream(), ROOT.resolve(username + FileUtil.SUFFIX));
        } catch (IOException e) {
            throw new JiraFileUploadException("Không thể lưu được file. Lỗi: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String fileId) {
        try {
            Path file = ROOT.resolve(fileId + FileUtil.SUFFIX);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new JiraFileUploadException("Không đọc được mã file trùng khớp");
            }
        } catch (MalformedURLException e) {
            throw new JiraFileUploadException("Lỗi: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(ROOT.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(ROOT, 1)
                    .filter(path -> !path.equals(ROOT))
                    .map(ROOT::relativize);
        } catch (IOException e) {
            throw new JiraFileUploadException("Không thể load hết files");
        }
    }
}
