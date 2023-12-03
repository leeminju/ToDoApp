package com.sparta.board2.repository;

import com.sparta.board2.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRepositoryTest extends RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("user 저장")
    void test1() {
        //given
        User user = new User("user", "password");

        //when-then
        User saved = userRepository.saveAndFlush(user);

        assertEquals(user, saved);

    }

    @Test
    // @Transactional
    @DisplayName("password 없이 user 저장")
    void test2() {
        //given
        User user = User.builder().username("user").build();

        //when-then
        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(user));

    }

}