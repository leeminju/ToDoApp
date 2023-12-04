package com.sparta.board2.entity;

import com.sparta.board2.dto.SignupRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    @DisplayName("new User(String username, String password)")
    void test1() {
        //given
        String username = "username";
        String password = "password";

        //when
        User user = new User(username, password);
        //then
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }

    @DisplayName("회원가입 요청 DTO 생성")
    @Nested
    class createSignupRequestDTO {
        @Test
        @DisplayName("회원가입 요청 DTO 생성 실패 잘못된 username")
        void test2() {
            // given
            String username = "USER123";
            String password = "Password123";
            SignupRequestDto signupRequestDto = new SignupRequestDto(username, password);

            // when
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting("message").contains("4자 이상의 10자 이하의 숫자, 영어 소문자를 포함한 Username을 입력해주세요.");
        }


        @Test
        @DisplayName("회원가입 요청 DTO 생성 실패 잘못된 password")
        void test3() {
            // given
            String username = "user123";
            String password = "password123";
            SignupRequestDto signupRequestDto = new SignupRequestDto(username, password);

            // when
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                    .extracting("message")
                    .contains("8자 이상의 15자 이하의 숫자, 영어 소문자, 대문자를 포함한 Password를 입력해주세요.");
        }

        @Test
        @DisplayName("회원가입 요청 DTO 생성 성공")
        void test4() {
            // given
            String username = "user123";
            String password = "Password123";
            SignupRequestDto signupRequestDto = new SignupRequestDto(username, password);

            // when
            Set<ConstraintViolation<SignupRequestDto>> violations = validate(signupRequestDto);

            // then
            assertThat(violations).isEmpty();
        }
    }

    private Set<ConstraintViolation<SignupRequestDto>> validate(SignupRequestDto signupRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(signupRequestDto);
    }
}