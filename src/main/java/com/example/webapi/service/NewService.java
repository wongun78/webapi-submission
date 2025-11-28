package com.example.webapi.service;

import java.util.List;

import com.example.webapi.domain.dto.news.NewRequest;
import com.example.webapi.domain.dto.news.NewResponse;

public interface NewService {

    List<NewResponse> getAll();

    NewResponse getById(Long id);

    NewResponse create(NewRequest request);

    NewResponse update(Long id, NewRequest request);

    void delete(Long id);
}