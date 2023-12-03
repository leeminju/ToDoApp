package com.sparta.board2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board2.config.WebSecurityConfig;
import com.sparta.board2.dto.LoginRequestDto;
import com.sparta.board2.dto.SignupRequestDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.jwt.JwtUtil;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {UserController.class, HomeController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserControllerTest {
    private MockMvc mvc;
    private Principal mockPrincipal;
    private User testUser;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    JwtUtil jwtUtil;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
        mockUserSetup();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "minju1234";
        String password = "Minju1234";

        testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("로그인 Page")
    void loginPage() throws Exception {
        // when - then
        mvc.perform(get("/api/user/login-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("회원가입 Page")
    void signupPage() throws Exception {
        // when - then
        mvc.perform(get("/api/user/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    @DisplayName("메인 Page")
    void mainPage() throws Exception {
        // when - then
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("회원 가입")
    void signupTest() throws Exception {
        // given
        SignupRequestDto requestDto = new SignupRequestDto("tester1", "Password1");

        String signupInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        // when - then
        mvc.perform(post("/api/user/signup")
                        .content(signupInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인")
    void loginTest() throws Exception {
        // given
        String username = "tester1";
        LoginRequestDto requestDto = new LoginRequestDto(username, "Password1");
        String loginInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        given(jwtUtil.createToken(username)).willReturn("Bearer Token");

        //when
        mvc.perform(post("/api/user/login")
                        .content(loginInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(header().string(JwtUtil.AUTHORIZATION_HEADER, "Bearer Token"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("사용자 정보 받기")
    void getUserInfoTest() throws Exception {
        // given

        // when - then
        mvc.perform(get("/api/user-info")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }
}

