package com.example.riraproject.file.service;

import com.example.riraproject.common.exception.RiraFileUploadException;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.file.util.FileUtil;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
import java.util.stream.Stream;

public interface FileService {
    void init();
    String save(MultipartFile file);
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
    private final MessageSource messageSource;
    private final JwtUtil jwtUtil;

    @Override
    public void init() {
        try {
            Files.createDirectories(ROOT);
            log.info("Created ROOT for file uploads");
        } catch (IOException e) {
           throw new RiraFileUploadException(MessageUtil.getMessage(messageSource, "file.uncreated"));
        }
    }

    @Transactional
    @Override
    public String save(MultipartFile file){
        String username = jwtUtil.getAuthenticatedUsername();
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
            throw new RiraFileUploadException(MessageUtil.getMessage(messageSource, "file.unsaved"));
        }
        User user = userService.findByUsername(username);
        String avatarUrl = FileUtil.URL + username;
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
                throw new RiraFileUploadException(MessageUtil.getMessage(messageSource, "file.load.error"));
            }
        } catch (MalformedURLException e) {
            throw new RiraFileUploadException("Lỗi: " + e.getMessage());
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
            throw new RiraFileUploadException(MessageUtil.getMessage(messageSource, "file.load-all.error"));
        }
    }
}
