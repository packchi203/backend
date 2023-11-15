package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {
    @NotBlank
    private int type;
}
