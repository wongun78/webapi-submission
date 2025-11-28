package com.example.webapi.domain.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name must not be blank")
    private String name;

    @NotBlank(message = "Category slug must not be blank")
    private String slug;
}