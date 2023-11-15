package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.StatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateAccountDto {
    private String role; //USER - ADMIN
    private StatusEnum status;
}
