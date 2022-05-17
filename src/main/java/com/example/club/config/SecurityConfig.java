package com.example.club.config;

import com.example.club.security.handler.ClubLoginSuccessHandler;
import com.example.club.security.service.ClubUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        //사용자 계정은 user1
//        auth.inMemoryAuthentication().withUser("user1")
//                .password("$2a$10$rfbQ1juTxR/WWlWZgbswq.R/lBanuLSvUhY7ZKBtetjb0tDph6/qa")
//                .roles("USER");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()// /sample/all에 모두 접근할 수 있도록 설정
                .antMatchers("/sample/member").hasRole("USER");// /sample/member에 'USER'권한이 있는 사용자만 접근할 수 있도록 설정

        http.formLogin();//인가나 인증 시에 문제가 생기면 로그인 화면을 보여준다.
        http.csrf().disable();//CSRF 토큰 발행하지 않도록 설정....외부에서 REST 방식으로 이용할 수 있는 보안 설정 이용할 것임.
        //http.logout();//로그아웃 처리...csrf().disable() 처리 때문에 GET 방식..아닐 때는 PUT 방식..
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);//7일간 쿠키 유지...Remember me 체크 박스 생성
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
