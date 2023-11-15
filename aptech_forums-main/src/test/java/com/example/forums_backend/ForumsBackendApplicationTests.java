package com.example.forums_backend;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.service.VoteService;
import com.example.forums_backend.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ForumsBackendApplicationTests {
    @Autowired
    VoteService voteService;
    @Autowired
    AccountRepository accountRepository;

    @Test
    void contextLoads() {
        Long id = Long.valueOf(1);
        voteService.delete(id);
    }

    @Test
    void getToken() {
        Long id  = Long.valueOf(3);
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(accountOptional.isPresent()){
            Account account = accountOptional.get();
            String token = JwtUtil.generateToken(account.getEmail(), account.getRole(), "APTECH", JwtUtil.ONE_DAY * 14);
            System.out.println(token);
        }
        System.out.println();
    }
}
