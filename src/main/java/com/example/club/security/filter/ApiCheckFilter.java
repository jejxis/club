package com.example.club.security.filter;

import com.example.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("REQUESTURI: "+request.getRequestURI());
        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("ApiCheckFilter............................................");
            log.info("ApiCheckFilter............................................");
            log.info("ApiCheckFilter............................................");

            boolean checkHeader = checkAuthHeader(request);//Authorization 이라는 헤더값 확인 후 boolean 값 반환
            if(checkHeader){
                filterChain.doFilter(request, response);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                //json 반환 빛 한글깨짐 수정
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");//403 에러 메시지 만들기
                json.put("message",message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }


        filterChain.doFilter(request, response);//다음 필터 단계로 넘어가는 역할 위해 필요
    }

    private boolean checkAuthHeader(HttpServletRequest request){//Authorization 헤더 추출해서 검증
        boolean checkResult = false;
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){//Authorization 헤더 메시지의 경우 앞에 인증 타입을 이용하는데 일반->Basic , JWT -> 'Bearer'
            log.info("Authorization exist: "+authHeader);
            try{
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result: "+email);
                checkResult = email.length() > 0;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return checkResult;
    }
}
