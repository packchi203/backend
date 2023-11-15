package com.example.forums_backend.service;

import com.example.forums_backend.dto.AccountUpdateDto;
import com.example.forums_backend.dto.UserAllInfoDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AccountManagerService {
    @Autowired
    AccountRepository accountRepository;


    public List<UserAllInfoDto> findAll() {
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream()
                .distinct()
                .map(this::fromEntityAccountDto)
                .collect(Collectors.toList());
    }

    public Account update(AccountUpdateDto account, Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        Account accountModal = optionalAccount.get();
        accountModal.setRole(account.getRole());
        accountModal.setStatus(account.getStatus());
        return accountRepository.save(accountModal);
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
    public UserAllInfoDto fromEntityAccountDto(Account account){
        return UserAllInfoDto.builder()
                .id(account.getId())
                .name(account.getName())
                .username(account.getUsername())
                .email(account.getEmail())
                .avatar(account.getImageUrl())
                .statusEnum(account.getStatus())
                .role(account.getRole())
                .createdAt(account.getCreatedAt())
                .reputation(account.getReputation())
                .build();
    }
}
