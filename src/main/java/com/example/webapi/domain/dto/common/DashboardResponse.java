package com.example.webapi.domain.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalNews;
    private Map<String, Long> newsByStatus; // DRAFT: 5, PUBLISHED: 10
    private Map<String, Long> topCategories; // Category name: count
}
