package com.example.forums_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class LoginDto {
    @NotNull(message = "email shouldn't be null")
    @NotBlank(message = "email is empty")
    private String email;
    @NotNull(message = "password shouldn't be null")
    @NotBlank(message = "password is empty")
    private String password;
}
