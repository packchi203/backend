package com.example.forums_backend.dto;

import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsByTagDto {
    private TagFollowResDto tag_details;
    private List<PostResDto> posts;
}
