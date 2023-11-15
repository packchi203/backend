package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResDto {
    private VoteType voteType;
    private int vote_count;
}
