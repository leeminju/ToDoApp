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
            ArrayList<String> message = new ArrayList<>();
            message.add("중복된 사용자가 존재합니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        // 사용자 등록
        User user = new User(username, password);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("회원가입 성공" + HttpStatus.OK.value());
    }

    public List<String> getAllUsername(User login_user) {
        List<User> users = userRepository.findAllByOrderByUsername();//이름순으로
        List<String> usernameList = new ArrayList<>();

        for (User user : users) {
            if (!login_user.getUsername().equals(user.getUsername()))
                usernameList.add(user.getUsername());
        }
        return usernameList;//나를 제외한 다른 사용자 리스트 가져옴
    }
}
