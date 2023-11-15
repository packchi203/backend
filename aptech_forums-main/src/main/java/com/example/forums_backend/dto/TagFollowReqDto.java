package com.example.forums_backend.dto;

import com.example.forums_backend.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagFollowReqDto {
    private Tag tag;
}
