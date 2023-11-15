package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long> {
    Optional<Post> findFirstBySlug(String slug);

    Optional<Post> findByIdAndAuthor_Id(Long id, Long author_id);

    List<Post> findByAuthor_id(Long id, Sort sort);
    List<Post> findByTagsIn(Collection<Tag> tags, Sort sort);
    @Query("select p from Post p order by p.comment.size + p.bookmarks.size + p.voteCount desc")
    List<Post> findAllPopular();
    @Query("select p from Post p where p.title like concat('%',:query,'%')  order by p.comment.size + p.bookmarks.size + p.voteCount desc")
    List<Post> searchAllPopular(String query);

}
