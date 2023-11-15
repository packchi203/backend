package com.example.forums_backend.api.admin;

import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.PostRepository;
import com.example.forums_backend.repository.TagRepository;
import com.example.forums_backend.service.AccountService;
import com.example.forums_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.forums_backend.config.constant.route.AdminRoute.*;

@RestController
@RequestMapping(PREFIX_ADMIN_ROUTE)
@RequiredArgsConstructor
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    AccountService accountService;

    @RequestMapping(value = DASHBOARD_COUNT_TOTAL, method = RequestMethod.GET)
    public ResponseEntity<?> getAll(){
        boolean isAdmin = accountService.adminCheck();
        if(!isAdmin){
            return ResponseEntity.status(403).body("Ban chưa được cấp quyền...");
        }
        return ResponseEntity.ok(dashboardService.countTotal());
    }
}
