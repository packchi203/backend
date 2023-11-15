package com.example.forums_backend.entity;

import com.example.forums_backend.entity.my_enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @Column(columnDefinition = "text")
    private String content;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private Comment parent;
    @OneToMany(mappedBy ="parent" ,cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Comment> reply_to = new HashSet<Comment>();
    @Column(columnDefinition = "int(11) default 0")
    private int voteCount;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Voting> voting = new HashSet<>();
    @OneToMany(mappedBy = "comment",cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Bookmark> bookmarks = new HashSet<>();
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private StatusEnum status;
}
