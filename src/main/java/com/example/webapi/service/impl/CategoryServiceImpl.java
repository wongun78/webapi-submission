package com.example.webapi.service.impl;

import com.example.webapi.domain.dto.category.CategoryRequest;
import com.example.webapi.domain.dto.category.CategoryResponse;
import com.example.webapi.domain.entity.Category;
import com.example.webapi.exception.BadRequestException;
import com.example.webapi.exception.ResourceNotFoundException;
import com.example.webapi.domain.entity.New;
import com.example.webapi.repository.CategoryRepository;
import com.example.webapi.repository.NewRepository;
import com.example.webapi.service.CategoryService;
import com.example.webapi.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final NewRepository newRepository;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setId(category.getId());
                    response.setName(category.getName());
                    response.setSlug(category.getSlug());
                    return response;
                })
                .toList();
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setSlug(category.getSlug());
        return response;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category name already exists");
        }
        
        // Auto-generate slug from name if not provided
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugUtil.toSlug(request.getName());
        }
        
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Category slug already exists: " + slug);
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(slug);
        
        Category savedCategory = categoryRepository.save(category);
        
        CategoryResponse response = new CategoryResponse();
        response.setId(savedCategory.getId());
        response.setName(savedCategory.getName());
        response.setSlug(savedCategory.getSlug());
        return response;
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Category name already exists");
        }

        // Auto-generate slug from name if not provided
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugUtil.toSlug(request.getName());
        }
        
        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw new BadRequestException("Category slug already exists: " + slug);
        }

        category.setName(request.getName());
        category.setSlug(slug);

        Category savedCategory = categoryRepository.save(category);
        
        CategoryResponse response = new CategoryResponse();
        response.setId(savedCategory.getId());
        response.setName(savedCategory.getName());
        response.setSlug(savedCategory.getSlug());
        return response;
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        
        List<New> newsList = newRepository.findByCategoryId(id);
        if (!newsList.isEmpty()) {
            throw new BadRequestException(
                "Cannot delete category. There are " + newsList.size() + 
                " news item(s) using this category. Please remove or reassign them first."
            );
        }
        
        categoryRepository.deleteById(id);
    }

}