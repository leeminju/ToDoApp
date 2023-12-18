package com.sparta.board2.user;

import com.sparta.board2.global.jwt.JwtUtil;
import com.sparta.board2.global.response.CustomResponseEntity;
import com.sparta.board2.global.security.UserDetailsImpl;
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

    @GetMapping("/users/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/users/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/users/signup")
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

    @PostMapping("/users/login")
    public ResponseEntity<CustomResponseEntity> login(@Valid @RequestBody LoginRequestDto userRequestDto) {
        userService.login(userRequestDto);

        String token = jwtUtil.createToken(userRequestDto.getUsername());

        return ResponseEntity.ok()
                .header(JwtUtil.AUTHORIZATION_HEADER, token)
                .body(new CustomResponseEntity("로그인 성공", HttpStatus.OK.value()));
    }
}
