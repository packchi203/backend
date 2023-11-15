package com.example.forums_backend.dto;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Comment;
import com.example.forums_backend.entity.my_enum.VoteType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResDto {
    private  Long id;
    private Account account;
    private String content;
    private boolean isVote;
    private boolean isBookmark;
    private PostResDto post;
    private VoteType voteType;
    private int voteCount;
    private boolean isChildren;
    private boolean myComment;
    private LocalDateTime createdAt;
    private List<CommentResDto> reply;
    private CommentResDto parent;
}
