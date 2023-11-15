package com.example.forums_backend.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull(message = "name shouldn't be null")
    @NotBlank
    private String name;
    @Email(message = "invalid email address")
//    @UniqueElements
    private String email;
}
