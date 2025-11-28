package com.example.webapi.domain.dto.news;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewResponse {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String userName;
}