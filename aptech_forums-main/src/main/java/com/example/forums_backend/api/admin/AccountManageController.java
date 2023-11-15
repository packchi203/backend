package com.example.forums_backend.api.admin;

import com.example.forums_backend.dto.AccountUpdateDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.service.AccountManagerService;
import com.example.forums_backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.forums_backend.config.constant.route.AdminRoute.*;

@RestController
@RequestMapping(PREFIX_ADMIN_ROUTE)
@RequiredArgsConstructor
public class AccountManageController {
    @Autowired
    AccountManagerService accountManagerService;

    @Autowired
    AccountRepository repository;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = ACCOUNT_PATH, method = RequestMethod.GET)
    public ResponseEntity<?> getListUser(){
        boolean isAdmin = accountService.adminCheck();
        if(!isAdmin){
            return ResponseEntity.status(403).body("Ban chưa được cấp quyền...");
        }
        return ResponseEntity.ok(accountManagerService.findAll());
    }

    @RequestMapping(value = ACCOUNT_PATH_WITH_ID, method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody AccountUpdateDto account, @PathVariable Long id) {
        boolean isAdmin = accountService.adminCheck();
        if(!isAdmin){
            return ResponseEntity.status(403).body("Ban chưa được cấp quyền...");
        }
        return ResponseEntity.ok(accountManagerService.update(account, id));
    }

    @RequestMapping(value = ACCOUNT_PATH_WITH_ID, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        boolean isAdmin = accountService.adminCheck();
        if(!isAdmin){
            return ResponseEntity.status(403).body("Ban chưa được cấp quyền...");
        }
        accountManagerService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
//    @RequestMapping(value = ACCOUNT_PATH, method = RequestMethod.GET)
//    public Page<Account> findPage(@RequestParam int page, @RequestParam int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        return repository.findAll(pageRequest);
//    }
}