package com.prac.join.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    //언제까지 유효한지
    public static String createToken(String userName, String key, Long expiredTimeMs){
        Claims claims = Jwts.claims(); //일종의 Map이다 Claims로 정보를 담을 수 있다.
        claims.put("userName", userName); //userName을 claims에 담는다

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //시작시간
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs)) //만료시간
                .signWith(SignatureAlgorithm.HS256,key) //어떤 알고리즘을 사용할지
                .compact();
    }
}
