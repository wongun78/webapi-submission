package com.example.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.example.webapi.util.constant.NewStatusEnum;

@Entity
@Table(name = "news")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class New {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = NewStatusEnum.DRAFT; 
        }
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}