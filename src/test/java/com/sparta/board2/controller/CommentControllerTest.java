package com.sparta.board2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board2.config.WebSecurityConfig;
import com.sparta.board2.dto.CommentRequestDto;
import com.sparta.board2.dto.CommentResponseDto;
import com.sparta.board2.entity.User;
import com.sparta.board2.security.UserDetailsImpl;
import com.sparta.board2.service.CommentService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class CommentControllerTest {
    private MockMvc mvc;
    private User testUser;
    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
        mockCommentSetup();
    }


    private void mockCommentSetup() {
        // Mock 테스트 유져 생성
        String username = "minju1234";
        String password = "Minju1234";

        testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("댓글 등록")
    void createCommentTest() throws Exception {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 생성");


        String commentInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        // when - then
        mvc.perform(post("/api/post/{post_id}/comment", 1)
                        .content(commentInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("댓글 조회")
    void getCommentTest() throws Exception {
        // given
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        given(commentService.getComment(1L))
                .willReturn(new CommentResponseDto(1L, "댓글 내용!!", testUser.getUsername(), localDateTime, localDateTime));

        // when - then
        mvc.perform(get("/api/comment/{comment_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 전체 조회")
    void getCommentsTest() throws Exception {
        // given
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        commentResponseDtos.add(new CommentResponseDto(1L, "내용 1", "user1", localDateTime.minusHours(3), localDateTime.minusMinutes(1)));
        commentResponseDtos.add(new CommentResponseDto(2L, "내용 2", "user2", localDateTime.minusMinutes(50), localDateTime.minusMinutes(2)));
        commentResponseDtos.add(new CommentResponseDto(3L, "내용 3", "user1", localDateTime.minusMinutes(10), localDateTime.minusMinutes(3)));

        given(commentService.getComments(1L))
                .willReturn(commentResponseDtos);

        // when - then
        mvc.perform(get("/api/post/{post_id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateCommentTest() throws Exception {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정");

        String commentInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        // when - then
        mvc.perform(put("/api/comment/{comment_id}", 1)
                        .content(commentInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() throws Exception {
        // given

        // when - then
        mvc.perform(delete("/api/comment/{comment_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }
}