package com.example.jiraproject.common.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Object content;
    private boolean hasErrors;
    private List<String> errors;
    private String timeStamp;
    private int statusCode;
}
