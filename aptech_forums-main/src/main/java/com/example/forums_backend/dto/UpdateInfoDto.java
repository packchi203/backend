package com.example.forums_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class UpdateInfoDto {
    private String imageUrl; //null hoặc sử dụng link ảnh
    private String name;
    private String bio;
    private String username;
    private String skill;
    private String github_username;
    private String education;
    private String email_display;
    private  String web_url;
}
