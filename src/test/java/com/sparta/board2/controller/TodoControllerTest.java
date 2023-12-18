package com.sparta.board2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.board2.global.config.WebSecurityConfig;
import com.sparta.board2.todo.TodoController;
import com.sparta.board2.todo.TodoRequestDto;
import com.sparta.board2.todo.TodoResponseDto;
import com.sparta.board2.user.User;
import com.sparta.board2.global.security.UserDetailsImpl;
import com.sparta.board2.todo.TodoService;
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
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {TodoController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class TodoControllerTest {
    private MockMvc mvc;
    private User testUser;
    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    TodoService todoService;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
        mockTodoSetup();
    }


    private void mockTodoSetup() {
        // Mock 테스트 유져 생성
        String username = "minju1234";
        String password = "Minju1234";

        testUser = new User(username, password);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("할일 등록")
    void createTodoTest() throws Exception {
        // given
        TodoRequestDto requestDto = new TodoRequestDto("할일 제목", "할일 내용");

        String todoInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        // when - then
        mvc.perform(post("/api/posts")
                        .content(todoInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("할일 조회")
    void getTodoByIdTest() throws Exception {
        // given
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        given(todoService.getTodoById(1L))
                .willReturn(new TodoResponseDto(1L, "할일 제목", testUser.getUsername(), "할일 내용", localDateTime.minusHours(1), localDateTime, false));

        // when - then
        mvc.perform(get("/api/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할일 전체 조회")
    void getTodoListTest() throws Exception {
        // given
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Map<String, List<TodoResponseDto>> map = new TreeMap<>();
        User user1 = new User("user1", "password");
        User user2 = new User("user2", "password");

        List<TodoResponseDto> user1TodoList = new ArrayList<>();
        user1TodoList.addAll(List.of(new TodoResponseDto[]{
                new TodoResponseDto(1L, "user1 할일 제목 1", user1.getUsername(), "내용1", localDateTime.minusHours(2), localDateTime.minusMinutes(10), false),
                new TodoResponseDto(3L, "user1 할일 제목 2", user1.getUsername(), "내용2", localDateTime.minusHours(1), localDateTime.minusMinutes(4), true)}
        ));

        List<TodoResponseDto> user2TodoList = new ArrayList<>();
        user2TodoList.addAll(List.of(new TodoResponseDto[]{
                new TodoResponseDto(2L, "user2 할일 제목 1", user1.getUsername(), "내용2", localDateTime.minusHours(1).minusMinutes(10), localDateTime.minusMinutes(30), true),
                new TodoResponseDto(4L, "user2 할일 제목 2", user1.getUsername(), "내용2", localDateTime.minusMinutes(40), localDateTime.minusMinutes(10), false)}
        ));
        map.put(user1.getUsername(), user1TodoList);
        map.put(user2.getUsername(), user2TodoList);

        given(todoService.getTodoList()).willReturn(map);

        // when - then
        mvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("할일 수정")
    void updateTodoTest() throws Exception {
        // given
        TodoRequestDto requestDto = new TodoRequestDto("할일 제목", "할일 수정");

        String todoInfo = objectMapper.writeValueAsString(requestDto);//class -> json string으로 바꾸기

        // when - then
        mvc.perform(put("/api/posts/{id}", 1)
                        .content(todoInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할일 완료 처리")
    void updateFisishedTest() throws Exception {
        // given
        boolean finished = true;
        given(todoService.updateFinished(1L, finished, testUser))
                .willReturn(finished);

        // when - then
        mvc.perform(put("/api/posts/{id}/finished/{finished}", 1, finished)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할일 완료 취소 처리")
    void updateUnfinsishedTest() throws Exception {
        // given
        boolean finished = false;
        given(todoService.updateFinished(1L, finished, testUser))
                .willReturn(finished);

        // when - then
        mvc.perform(put("/api/posts/{id}/finished/{finished}", 1, finished)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("할일 삭제")
    void deleteTodoTest() throws Exception {
        // given

        // when - then
        mvc.perform(delete("/api/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk());
    }

}