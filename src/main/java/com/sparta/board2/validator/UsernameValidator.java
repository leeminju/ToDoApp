package com.sparta.board2.validator;

import com.sparta.board2.annotation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.MessageFormat;

public class UsernameValidator implements ConstraintValidator<Username, String> { // 1
    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 10;
    private static final String regexUsername = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{" + MIN_SIZE + "," + MAX_SIZE + "}+$";


    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        boolean isValidUsername = username.matches(regexUsername);
        if (!isValidUsername) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영어 소문자를 포함한 Username을 입력해주세요.",
                            MIN_SIZE,
                            MAX_SIZE)).addConstraintViolation();
        }
        return isValidUsername;
    }
}