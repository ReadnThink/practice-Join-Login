package com.prac.join.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.join.domain.dto.UserJoinRequest;
import com.prac.join.domain.dto.UserLoginRequest;
import com.prac.join.exception.AppException;
import com.prac.join.exception.ErrorCode;
import com.prac.join.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;
    String name = "solbae";
    String password = "1234";

    @Test
    @WithMockUser
    void 회원가입_성공() throws Exception {

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(name, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void 회원가입_실패_아이디중복() throws Exception {

        when(userService.join(any())).thenThrow(new RuntimeException("해당 user가 중복됩니다."));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(name, password))))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void 로그인_성공() throws Exception {

        when(userService.login(any())).thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser
    void 로그인_아이디없음() throws Exception {


        when(userService.login(any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser
    void 로그인_패스워드불일치() throws Exception {
        when(userService.login(any())).thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
}