package com.prac.join.service;

import com.prac.join.domain.User;
import com.prac.join.domain.dto.UserJoinRequest;
import com.prac.join.domain.dto.UserLoginRequest;
import com.prac.join.exception.AppException;
import com.prac.join.exception.ErrorCode;
import com.prac.join.repository.UserRepository;
import com.prac.join.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}") //yml파일에 환경변수로 설정 실제는 환경설정에 설정해둠
    private String key;

    private Long expireTimeMs = 1000 * 60 * 60L;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    //회원가입기능
    public String join(UserJoinRequest userJoinRequest) {

        //중복확인
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent((user -> {
                    throw new AppException(ErrorCode.DUPLICATE_USER_NAME, userJoinRequest.getUserName() +"는 이미 있습니다.");
                }));
        //중복 없으면 회원가입 성공
        User user = User.builder()
                .userName(userJoinRequest.getUserName())
                .password(encoder.encode(userJoinRequest.getPassword()))
                .build();
        userRepository.save(user);

        return "회원등록이 완료되었습니다.";
    }

    public String login(UserLoginRequest userLoginRequest) {
        //userName없음
        User savedUser = userRepository.findByUserName(userLoginRequest.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userLoginRequest.getUserName() + "이 없습니다."));
        //password틀림
        if (!encoder.matches(userLoginRequest.getPassword(), savedUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "password가 잘못되었습니다.");
        }
        String token = JwtUtil.createToken(savedUser.getUserName(), key, expireTimeMs);
        //토큰발행

        return token;
    }
}
