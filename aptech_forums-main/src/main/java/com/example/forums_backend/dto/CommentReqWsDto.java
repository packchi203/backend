package com.example.forums_backend.dto;

import com.example.forums_backend.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReqWsDto {
    @NotBlank
    private String content;
    private Comment reply_to;
    private Long postId;
}
