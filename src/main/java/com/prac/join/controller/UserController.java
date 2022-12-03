package com.prac.join.controller;

import com.prac.join.domain.dto.UserJoinRequest;
import com.prac.join.domain.dto.UserLoginRequest;
import com.prac.join.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest userJoinRequest) {
        return ResponseEntity.ok().body(userService.join(userJoinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest);
        return ResponseEntity.ok().body(token);
    }
}
