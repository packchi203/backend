package com.example.forums_backend.dto;

import com.example.forums_backend.entity.Badge;
import com.example.forums_backend.entity.UserBadge;
import com.example.forums_backend.entity.UserContact;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ProfileDto {
    private UserAllInfoDto info;
    private List<UserBadge> badges;
    private List<UserContact> contacts;
}
