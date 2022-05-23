package com.example.club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
    private String secretKey = "zerock12345678";//Signature 생성

    //1month
    private long expire = 60 * 24 * 30;//만료 기간

    public String generateToken(String content) throws Exception{//JWT 토큰 생성
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                //.setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant())) // 만료 기간을 1초로 설정..유효 기간이 자나서 오류 발생
                .claim("sub", content)//sub라는 이름의 Claim에 사용자의 이메일 주소 입력... 나중에 사용할 수 있도록..
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception{//인코딩된 문자열에서 원하는 값을 추출..JWT 문자열 검증..기간 만료 시 예외 발생, sub 이름으로 보관된 이메일 반환
        String contentValue = null;
        try{
            DefaultJws defaultJws= (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8"))
                    .parseClaimsJws(tokenStr);
            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("--------------------------");
            contentValue = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }
}
