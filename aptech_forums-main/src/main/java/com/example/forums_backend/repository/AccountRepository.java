package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmail(String email);
    Optional<Account> findByEmail(String email);
    Optional<Account> findFirstByUsername(String username);

    List<Account> findByNameContaining(String query);
    @Query("select p from Account p where p.username like concat(:query,'%') ")
    List<Account> searchAccountByUserName(String query);
}
