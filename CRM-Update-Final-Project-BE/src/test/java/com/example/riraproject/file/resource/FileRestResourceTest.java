package com.example.riraproject.file.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.file.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FileRestResourceTest {
    @Mock private FileService service;
    @Autowired private MessageSource messageSource;
    private FileRestResource restResource;
    @Mock private Resource resource;
    @Mock private MultipartFile file;

    @BeforeEach
    void init() {
        restResource = new FileRestResource(service, messageSource);
    }

    @Test
    void findByIdTest() {
        //SETUP
        String id = "fileID";
        Mockito.when(service.load(id)).thenReturn(resource);
        ResponseEntity<Resource> result = restResource.findById(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertEquals(resource, result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(MediaType.IMAGE_JPEG, result.getHeaders().getContentType());
    }

    @Test
    void uploadFileTest() {
        //SETUP
        String fileUrl = "avatar url";
        Mockito.when(service.save(file)).thenReturn(fileUrl);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.uploadFile(file);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto response = result.getBody();
        Assertions.assertEquals(fileUrl, response.getContent());
        Assertions.assertFalse(response.isHasErrors());
        Assertions.assertNull(response.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertNotNull(response.getTimeStamp());
        Mockito.verify(service).save(file);
    }

    @Test
    void deleteAllFilesTest() {
        //SETUP
        Mockito.doNothing().when(service).deleteAll();
        ResponseEntity<ResponseDto> result = restResource.deleteAllFiles();
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto response = result.getBody();
        Assertions.assertEquals(MessageUtil
                .getMessage(messageSource, "file.deleted"), response.getContent());
        Assertions.assertFalse(response.isHasErrors());
        Assertions.assertNull(response.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertNotNull(response.getTimeStamp());
        Mockito.verify(service).deleteAll();
    }
}
