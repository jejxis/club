package com.example.club.controller;

import com.example.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {
    @GetMapping("/all")
    public void exAll(){//누구나 접근 가능
        log.info("exAll..........");
    }

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember){//로그인한 사용자만 접근 가능
        log.info("exMember..........");
        log.info("-------------------------------------");
        log.info(clubAuthMember);
    }

    @GetMapping("/admin")
    public void exAdmin(){//관리자 권한이 있는 사용자만 접근 가능
        log.info("exAdmin..........");
    }
}