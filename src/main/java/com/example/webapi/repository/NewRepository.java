package com.example.webapi.repository;

import com.example.webapi.domain.entity.New;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface NewRepository extends JpaRepository<New, Long> {
    List<New> findByCategoryId(Long categoryId);
    boolean existsByTitle(String title);
    
    boolean existsByTitleAndIdNot(String title, Long id);
}