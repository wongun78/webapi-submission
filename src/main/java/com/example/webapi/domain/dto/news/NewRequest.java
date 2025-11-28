package com.example.webapi.domain.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewRequest {

    @NotBlank(message = "New title must not be blank")
    private String title;

    @NotBlank(message = "New content must not be blank")
    private String content;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;

    @NotNull(message = "User ID must not be null")
    private Long userId;
}