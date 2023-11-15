package com.example.forums_backend.api;

import com.example.forums_backend.dto.UpdateInfoDto;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.service.AccountManagerService;
import com.example.forums_backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.forums_backend.config.constant.route.AccountRoute.*;

@RestController
@RequestMapping(PREFIX_ACCOUNT_ROUTE)
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    AccountManagerService accountManagerService;
    @Autowired
    AccountService accountService;
    @RequestMapping(value = "/users/famous", method = RequestMethod.GET)
    public ResponseEntity<?> getUsersFamous()  {
        return ResponseEntity.ok(accountService.usersFamous());
    }

    @RequestMapping(value = UPDATE_PATH, produces = "application/json", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity<?> update(@RequestBody @Valid UpdateInfoDto updateInfoDto) throws AppException {
        return ResponseEntity.ok(accountService.updateInfoDto(updateInfoDto));
    }

    @RequestMapping(value = DELETE_PATH, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMyProfile(){
        return ResponseEntity.ok(accountService.deleteProfile());
    }

    @RequestMapping(value = USER_INFO_BY_USERNAME, method = RequestMethod.GET)
    public ResponseEntity<?> findUserInfoByUsername(@PathVariable String username) throws AppException {
        try {
            return ResponseEntity.ok(accountService.findUserInfoByUsername(username));
        }catch (AppException exception){
            return ResponseEntity.status(404).body("USER NOT FOUND!");
        }

    }
}
