package com.example.forums_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String icon;
    private String name; //not null
    @Column(unique = true)
    private String slug;
    @Column(columnDefinition = "text")
    private String description;
    private boolean important;
    private String color_bg;
    @Column(columnDefinition = "int(11) default 0")
    private int follow_count; // 0
    @JsonIgnore
    @ManyToMany(mappedBy = "tags", targetEntity = Post.class)
    @JsonIgnoreProperties("tags")
    Set<Post> posts = new HashSet<>();
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "account",  cascade = CascadeType.MERGE)
    Set<TagFollowing> tagFollowings = new HashSet<>();
}
