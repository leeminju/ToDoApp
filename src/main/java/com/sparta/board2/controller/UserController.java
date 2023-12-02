package com.sparta.board2.controller;

import com.sparta.board2.dto.LoginRequestDto;
import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.jwt.JwtUtil;
import com.sparta.board2.response.CustomResponseEntity;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public ResponseEntity<CustomResponseEntity> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).
                body(
                        new CustomResponseEntity(
                                "회원 가입 성공",
                                HttpStatus.CREATED.value()
                        )
                );
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public String getUsername(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser().getUsername();
    }

    @PostMapping("/user/login")
    public ResponseEntity<CustomResponseEntity> login(@Valid @RequestBody LoginRequestDto userRequestDto, HttpServletResponse response) {
        userService.login(userRequestDto);

        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userRequestDto.getUsername()));

        return ResponseEntity.ok().body(new CustomResponseEntity("로그인 성공", HttpStatus.OK.value()));
    }
}
