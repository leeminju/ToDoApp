package com.sparta.board2.service;

import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findById(username);
        if (checkUsername.isPresent()) {
            ArrayList<String> msg = new ArrayList<>();
            msg.add("중복된 username 입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공 ");
    }

}
