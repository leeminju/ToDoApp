package com.sparta.board2.service;

import com.sparta.board2.global.exception.RestApiException;
import com.sparta.board2.user.LoginRequestDto;
import com.sparta.board2.user.SignupRequestDto;
import com.sparta.board2.user.User;
import com.sparta.board2.user.UserRepository;
import com.sparta.board2.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    SignupRequestDto signupRequestDto;
    LoginRequestDto loginRequestDto;

    @BeforeEach
    void setUp() {

        signupRequestDto = new SignupRequestDto("user", "password");
        loginRequestDto = new LoginRequestDto("user", "password");
    }

    @Test
    @DisplayName("회원가입 테스트 - 중복된 회원")
    void signupTest() {
        //given
        User user = new User("user", "password");
        given(userRepository.findById("user")).willReturn(Optional.of(user));
        UserService userService = new UserService(userRepository, passwordEncoder);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.signup(signupRequestDto);
        });
        //then
        assertEquals("중복된 회원 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 테스트 - 성공")
    void signupTest2() {
        //given
        given(userRepository.findById("user")).willReturn(Optional.empty());

        UserService userService = new UserService(userRepository, passwordEncoder);
        User user = new User("user", "password");
        given(userRepository.save(any(User.class))).willReturn(user);
        //when
        String savedUsername = userService.signup(signupRequestDto);

        //then
        assertEquals("user", savedUsername);
    }

    @Test
    @DisplayName("로그인 테스트-등록된 회원 아님")
    void loginTest() {
        //given
        UserService userService = new UserService(userRepository, passwordEncoder);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.login(loginRequestDto);
        });

        //then
        assertEquals("등록된 회원이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 테스트-비밀번호 불일치")
    void loginTest2() {
        //given
        User user = new User("user", "password1");
        given(userRepository.findById("user")).willReturn(Optional.of(user));
        UserService userService = new UserService(userRepository, passwordEncoder);

        //when
        Exception exception = assertThrows(RestApiException.class, () -> {
            userService.login(loginRequestDto);
        });

        //then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 테스트-성공")
    void loginTest3() {
        //given
        User user = new User("user", "password");
        given(userRepository.findById("user")).willReturn(Optional.of(user));
        UserService userService = new UserService(userRepository, passwordEncoder);
        given(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).willReturn(true);

        //when
        boolean result = userService.login(loginRequestDto);

        //then
        assertTrue(result);
    }
}