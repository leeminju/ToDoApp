package com.sparta.board2.service;

import com.sparta.board2.dto.LoginRequestDto;
import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.repository.UserRepository;
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
            throw new IllegalArgumentException("중복된 회원 입니다.");
        }

        // 사용자 등록
        User user = new User(username, password);
        User savedUser = userRepository.save(user);

        return savedUser.getUsername();
    }

    public void login(LoginRequestDto loginRequestDto) {
		String username = loginRequestDto.getUsername();
		String password = loginRequestDto.getPassword();

		User user = userRepository.findById(username)
			.orElseThrow(() -> new IllegalArgumentException("등록된 회원이 없습니다."));

		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
	}
}
