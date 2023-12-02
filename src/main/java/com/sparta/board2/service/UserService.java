package com.sparta.board2.service;

import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private MessageSource messageSource;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findById(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 회원 입니다.");
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);
    }

}
