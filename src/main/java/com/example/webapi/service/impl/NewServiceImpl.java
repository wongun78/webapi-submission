package com.example.webapi.service.impl;

import com.example.webapi.domain.dto.common.DashboardResponse;
import com.example.webapi.domain.dto.news.NewRequest;
import com.example.webapi.domain.dto.news.NewResponse;
import com.example.webapi.domain.entity.Category;
import com.example.webapi.domain.entity.New;
import com.example.webapi.domain.entity.User;
import com.example.webapi.exception.BadRequestException;
import com.example.webapi.exception.ResourceNotFoundException;
import com.example.webapi.repository.CategoryRepository;
import com.example.webapi.repository.NewRepository;
import com.example.webapi.repository.UserRepository;
import com.example.webapi.service.NewService;
import com.example.webapi.util.constant.AppConstants;
import com.example.webapi.util.constant.NewStatusEnum;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewServiceImpl implements NewService {

    private final NewRepository newRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<NewResponse> getAll() {
        // Get current user authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isGuest = auth == null || !auth.isAuthenticated() || 
                         auth.getPrincipal().equals("anonymousUser");
        
        List<New> newsList;
        if (isGuest) {
            // Guest only sees PUBLISHED news
            newsList = newRepository.findByStatus(NewStatusEnum.PUBLISHED);
        } else {
            // Authenticated users (ADMIN/REPORTER) see all
            newsList = newRepository.findAll();
        }
        
        return newsList.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    private NewResponse mapToResponse(New news) {
        NewResponse response = new NewResponse();
        response.setId(news.getId());
        response.setTitle(news.getTitle());
        response.setContent(news.getContent());
        if (news.getCategory() != null) {
            response.setCategoryId(news.getCategory().getId());
            response.setCategoryName(news.getCategory().getName());
        }
        if (news.getUser() != null) {
            response.setUserId(news.getUser().getId());
            response.setUserName(news.getUser().getUsername());
        }
        return response;
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
        // Lấy user hiện tại từ JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Kiểm tra title trùng
        if (newRepository.existsByTitle(request.getTitle())) {
            throw new BadRequestException("News title already exists");
        }
        
        // Kiểm tra category tồn tại
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        New news = new New();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(category);
        news.setUser(currentUser); // Set author từ JWT
        // status sẽ tự động set = DRAFT trong @PrePersist

        New savedNews = newRepository.save(news);
        
        return mapToResponse(savedNews);
    }

    @Override
    public NewResponse update(Long id, NewRequest request) {
        // Lấy user hiện tại từ JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        New news = newRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));

        // Kiểm tra quyền: chỉ author hoặc admin mới được update
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(AppConstants.ROLE_ADMIN));
        
        if (!news.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to update this news");
        }

        // Kiểm tra title trùng (trừ chính nó)
        if (newRepository.existsByTitleAndIdNot(request.getTitle(), id)) {
            throw new BadRequestException("News title already exists");
        }
        
        // Kiểm tra category tồn tại
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());  
        news.setCategory(category);

        New savedNews = newRepository.save(news);

        return mapToResponse(savedNews);
    }

    @Override
    public void delete(Long id) {
        // Lấy user hiện tại từ JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        New news = newRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));

        // Kiểm tra quyền: chỉ author hoặc admin mới được delete
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals(AppConstants.ROLE_ADMIN));
        
        if (!news.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this news");
        }

        newRepository.deleteById(id);
    }

    @Override
    public NewResponse publishNews(Long id) {
        New news = newRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found"));
        
        // Chỉ có thể publish news có status DRAFT
        if (news.getStatus() != NewStatusEnum.DRAFT) {
            throw new BadRequestException("Only DRAFT news can be published");
        }
        
        news.setStatus(NewStatusEnum.PUBLISHED);
        New savedNews = newRepository.save(news);
        return mapToResponse(savedNews);
    }

    @Override
    public DashboardResponse getDashboard() {
        List<New> allNews = newRepository.findAll();
        
        // Tổng số news
        long totalNews = allNews.size();
        
        // Thống kê theo status
        Map<String, Long> newsByStatus = allNews.stream()
                .collect(Collectors.groupingBy(
                    news -> news.getStatus().name(),
                    Collectors.counting()
                ));
        
        // Top categories (top 5)
        Map<String, Long> topCategories = allNews.stream()
                .filter(news -> news.getCategory() != null)
                .collect(Collectors.groupingBy(
                    news -> news.getCategory().getName(),
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    HashMap::new
                ));
        
        return new DashboardResponse(totalNews, newsByStatus, topCategories);
    }

}