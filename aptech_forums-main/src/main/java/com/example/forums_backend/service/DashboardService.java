package com.example.forums_backend.service;

import com.example.forums_backend.dto.DashboardCountDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.entity.Tag;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    AccountRepository accountRepository;

    public DashboardCountDto countTotal(){
        List<Post> posts = postRepository.findAll();
        List<Account> accounts = accountRepository.findAll();
        List<Tag> tags = tagRepository.findAll();
        return DashboardCountDto.builder()
                .post_count(posts.size())
                .user_count(accounts.size())
                .tag_count(tags.size())
                .build();
    }
}
