package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findFirstBySlug(String slug);

    //Top 5 tag được sử dụng
    @Query("select p from Tag p order by p.posts.size + p.follow_count desc")
    List<Tag> findTagsPopular();
    @Query("select p from Tag p where p.name like concat('%',:query,'%')")
    List<Tag> searchTag(String query);
}
