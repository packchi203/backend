package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge,Long> {
}
