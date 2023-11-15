package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkReqDto {
    private Long subject_id;
    private Subject subject;
}
