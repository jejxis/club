package com.example.club.security;

import com.example.club.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTests {
    private JWTUtil jwtUtil;//JWTTests 클래스는 스프링 이용하는 테스트가 아니라 직접 JWTUtil 객체를 만들어 사용해야 함.

    @BeforeEach
    public void testBefore(){
        System.out.println("testBefore..............");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception{
        String email = "user95@zerock.org";
        String str = jwtUtil.generateToken(email);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception{
        String email = "user95@zerock.org";
        String str = jwtUtil.generateToken(email);
        Thread.sleep(5000);
        String resultEmail = jwtUtil.validateAndExtract(str);
        System.out.println(resultEmail);
    }
}
