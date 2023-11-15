package com.example.forums_backend.repository;

import com.example.forums_backend.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContactRepository extends JpaRepository<UserContact,Long> {
    List<UserContact> findByAccount_Id(Long id);
}
