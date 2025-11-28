package com.example.webapi.service.impl;

import com.example.webapi.domain.dto.news.NewRequest;
import com.example.webapi.domain.dto.news.NewResponse;
import com.example.webapi.domain.entity.Category;
import com.example.webapi.domain.entity.New;
import com.example.webapi.exception.BadRequestException;
import com.example.webapi.exception.ResourceNotFoundException;
import com.example.webapi.repository.CategoryRepository;
import com.example.webapi.repository.NewRepository;
import com.example.webapi.service.NewService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewServiceImpl implements NewService {

    private final NewRepository newRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<NewResponse> getAll() {
        return newRepository.findAll().stream()
                .map(news -> {
                    NewResponse response = new NewResponse();
                    response.setId(news.getId());
                    response.setTitle(news.getTitle());
                    response.setContent(news.getContent());
                    if (news.getCategory() != null) {
                        response.setCategoryId(news.getCategory().getId());
                        response.setCategoryName(news.getCategory().getName());
                    }
                    return response;
                })
                .toList();
    }

    @Override
    public NewResponse getById(Long id) {
        New news = newRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        
        NewResponse response = new NewResponse();
        response.setId(news.getId());
        response.setTitle(news.getTitle());
        response.setContent(news.getContent());
        if (news.getCategory() != null) {
            response.setCategoryId(news.getCategory().getId());
            response.setCategoryName(news.getCategory().getName());
        }
        return response;
    }

    @Override
    public NewResponse create(NewRequest request) {
        if (newRepository.existsByTitle(request.getTitle())) {
            throw new BadRequestException("News title already exists");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        New news = new New();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(category);

        New savedNews = newRepository.save(news);
        
        NewResponse response = new NewResponse();
        response.setId(savedNews.getId());
        response.setTitle(savedNews.getTitle());
        response.setContent(savedNews.getContent());
        if (savedNews.getCategory() != null) {
            response.setCategoryId(savedNews.getCategory().getId());
            response.setCategoryName(savedNews.getCategory().getName());
        }
        return response;
    }

    @Override
    public NewResponse update(Long id, NewRequest request) {
        New news = newRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        if (newRepository.existsByTitleAndIdNot(request.getTitle(), id)) {
            throw new BadRequestException("News title already exists");
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());  
        news.setCategory(category);

        New savedNews = newRepository.save(news);

        NewResponse response = new NewResponse();
        response.setId(savedNews.getId());
        response.setTitle(savedNews.getTitle());
        response.setContent(savedNews.getContent());
        if (savedNews.getCategory() != null) {
            response.setCategoryId(savedNews.getCategory().getId());
            response.setCategoryName(savedNews.getCategory().getName());
        }
        return response;
    }

    @Override
    public void delete(Long id) {
        if (!newRepository.existsById(id)) {
            throw new ResourceNotFoundException("News not found");
        }
        newRepository.deleteById(id);
    }

}