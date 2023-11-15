package com.example.forums_backend.repository;

import com.example.forums_backend.entity.PostView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostViewRepository extends JpaRepository<PostView,Long> {
    Optional<PostView> findFirstByPost_IdAndAccount_Id(Long postId, Long accountId);
}
