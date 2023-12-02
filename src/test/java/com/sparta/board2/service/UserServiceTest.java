package com.sparta.board2.service;

import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void setUp() {

        signupRequestDto = new SignupRequestDto("user", "password");
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

}