package com.example.jiraproject.file.service;

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
import java.util.UUID;
import java.util.stream.Stream;

public interface FileService {
    void init();
    void save(String userId, MultipartFile file);
    Resource load(String fileId);
    void deleteAll();
    Stream<Path> loadAll();
}
@Service
class FileServiceImpl implements FileService{
    private final Path root = Paths.get("./src/main/resources/uploads");

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
            System.out.println("created root");
        } catch (IOException e) {
//           throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(String userId, MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(userId));
            System.out.println("fileName: " + file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String fileId) {
        try {
            Path file = root.resolve(fileId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
