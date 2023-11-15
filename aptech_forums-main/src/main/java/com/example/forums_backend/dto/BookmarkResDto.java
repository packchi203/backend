package com.example.forums_backend.dto;

import com.example.forums_backend.entity.my_enum.Subject;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResDto {
    private Long id;
    private String content;
    private String url_redirect;
    @Enumerated(EnumType.ORDINAL)
    private Subject subject;
    private LocalDateTime createdAt;
}
