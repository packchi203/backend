package com.example.forums_backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoDto {
    private String avatar;
    private String name;
    private boolean fpt_member;
    private String email;
    private String role;
}
