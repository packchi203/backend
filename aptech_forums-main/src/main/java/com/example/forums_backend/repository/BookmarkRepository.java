package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Optional<Bookmark> findFirstByPost_IdAndAccount_Id(Long postId, Long accountId);
    Optional<Bookmark> findFirstByComment_IdAndAccount_Id(Long commentId, Long accountId);

    List<Bookmark> findAllByAccount_Id(Long id);
}
