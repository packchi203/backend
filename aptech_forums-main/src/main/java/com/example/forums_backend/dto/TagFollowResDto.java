package com.example.forums_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagFollowResDto {
    private Long id;
    private String name;
    private String slug;
    private String icon;
    private int tag_follow_count;
    private boolean isFollow;
    private int posts_use;
    private String desc;
    private boolean important;
    private String color_bg;
    private LocalDateTime createdAt;
}
