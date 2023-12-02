package com.sparta.board2.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board2.dto.LoginRequestDto;
import com.sparta.board2.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = jwtUtil.createToken(username);//토큰 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);//발급한 토큰을 Header에 추가

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html; charset=UTF-8");//한글 깨짐 방지
        PrintWriter writer = response.getWriter();
        writer.println("로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);//추가적으로 다른 것 보내도됨!
        response.setCharacterEncoding("UTF-8");//한글 깨짐 방지
        response.setContentType("text/html; charset=UTF-8");//한글 깨짐 방지
        PrintWriter writer = response.getWriter();
        writer.println("회원을 찾을 수 없습니다.");
    }
}

