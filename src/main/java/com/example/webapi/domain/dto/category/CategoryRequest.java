package com.example.webapi.domain.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name must not be blank")
    private String name;

    private String slug; // Auto-generated if not provided
}