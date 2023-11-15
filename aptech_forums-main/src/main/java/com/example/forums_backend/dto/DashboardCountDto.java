package com.example.forums_backend.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCountDto {
    public int post_count;
    public int user_count;
    public int tag_count;
}
