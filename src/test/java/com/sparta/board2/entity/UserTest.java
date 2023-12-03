package com.sparta.board2.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}