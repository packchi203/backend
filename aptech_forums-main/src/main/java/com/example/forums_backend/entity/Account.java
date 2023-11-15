package com.example.forums_backend.entity;

import com.example.forums_backend.entity.my_enum.AuthProvider;
import com.example.forums_backend.entity.my_enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String imageUrl; //null hoặc sử dụng link ảnh
    private String name;//not null
    @JsonIgnore
    private String bio;
    @JsonIgnore
    @Column(unique = true)
    private String email;// not null - unique
    @Column(unique = true)
    private String username;//not null - unique

    //info
    private String education;
    private String email_display;
    private  String web_url;

    //
    @JsonIgnore
    private String password; //null
    @JsonIgnore
    private boolean email_verify;//true - false
    private boolean fpt_member; // true - false
    private String skill;
    @Column(columnDefinition = "int(11) default 0")
    private int reputation; // default 0
    @JsonIgnore
    private String github_username;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private AuthProvider provider; // enum
    @JsonIgnore
    private String providerId;// null khi là local
    @JsonIgnore
    private String one_time_password; // null
    @JsonIgnore
    private Date expire_time; //null
    @JsonIgnore
    @Column(columnDefinition = "varchar(255) default 'USER'")
    private String role; //USER - ADMIN
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt; //null
    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt; //null
    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Post> posts = new HashSet<>(); //null
    @OneToMany(mappedBy = "account", cascade = CascadeType.MERGE)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Comment> comments  = new HashSet<>(); //null
    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<TagFollowing> tagFollowings = new HashSet<>(); // null
    @OneToMany(mappedBy = "account", cascade = CascadeType.MERGE)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<UserContact> userContacts = new HashSet<>(); //null
    @OneToMany(mappedBy = "account", cascade = CascadeType.MERGE)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<PostView> postViews  = new HashSet<>(); //null
    @OneToMany
    @JsonIgnore
    Set<UserBadge> userBadge = new HashSet<>();
    private StatusEnum status;
}
