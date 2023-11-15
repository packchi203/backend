package com.example.forums_backend.dto;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.entity.my_enum.VoteType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResDto {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private Set<Tag> tags;
    private Account account;
    private boolean isVote;
    private boolean isBookmark;
    private int commentCount;
    private int bookmarkCount;
    private int viewCount;
    private VoteType voteType;
    private int voteCount;
    private boolean isMyPost;
    private LocalDateTime createdAt;
}
