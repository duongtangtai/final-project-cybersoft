package com.example.jiraproject.file.service;

import com.example.jiraproject.common.exception.JiraFileUploadException;
import com.example.jiraproject.file.util.FileUtil;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

public interface FileService {
    void init();
    String save(String id, MultipartFile file);
    Resource load(String fileId);
    void deleteAll();
    Stream<Path> loadAll();
}
@Service
@Slf4j
@RequiredArgsConstructor
class FileServiceImpl implements FileService {
    public static final Path ROOT = Paths.get("./src/main/resources/uploads");
    private final UserService userService;

    @Override
    public void init() {
        try {
            Files.createDirectories(ROOT);
            log.info("Created ROOT for file uploads");
        } catch (IOException e) {
           throw new JiraFileUploadException("Could not initialize folder for upload!");
        }
    }

    @Transactional
    @Override
    public String save(String id, MultipartFile file){
        String fileName = id + FileUtil.SUFFIX;
        if (!ROOT.toFile().exists()) { //create ROOT if ROOT doesn't exist
            init();
        }
        try {
            if (ROOT.resolve(fileName).toFile().exists()) {
                log.info("OLD FILE EXISTED");
                Files.delete(ROOT.resolve(fileName));
                log.info("DELETED OLD FILE");
            }
            Files.copy(file.getInputStream(), ROOT.resolve(id + FileUtil.SUFFIX));
        } catch (IOException e) {
            throw new JiraFileUploadException("Không thể lưu được file. Lỗi: " + e.getMessage());
        }
        User user = userService.findUserById(UUID.fromString(id));
        String avatarUrl = FileUtil.URL + id;
        user.setAvatar(avatarUrl);
        return avatarUrl;
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
