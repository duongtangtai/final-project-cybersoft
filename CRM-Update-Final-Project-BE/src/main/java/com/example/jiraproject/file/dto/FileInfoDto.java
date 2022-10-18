package com.example.jiraproject.file.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {
    private String fileName;
    private String url;
}
