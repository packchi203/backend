package com.example.forums_backend.repository;

import com.example.forums_backend.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge,Long> {
    List<UserBadge> findByAccount_Id(Long id);
}
