package com.sparta.board2.user;

import com.sparta.board2.global.exception.ErrorCode;
import com.sparta.board2.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findById(username);
        if (checkUsername.isPresent()) {
            throw new RestApiException(ErrorCode.IS_DUPLICATE_USERNAME);
        }

        // 사용자 등록
        User user = new User(username, password);
        User savedUser = userRepository.save(user);

        return savedUser.getUsername();
    }

    public boolean login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findById(username)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RestApiException(ErrorCode.NOT_EQUALS_PASSWORD);
        }

        return true;
    }
}
