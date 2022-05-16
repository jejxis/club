package com.example.club.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode(){
        String password = "1111";
        String enPw = passwordEncoder.encode(password);//"1111" 암호화
        System.out.println("enPw: "+enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);//암호화한 결과가 "1111"에 맞는지 확인
        System.out.println("matchResult: "+matchResult);
    }
}
