package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입_정상() throws Exception {
        String signupRequest = """
            {
              "username": "JINHO",
              "password": "12341234",
              "nickname": "Mentos"
            }
            """;

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("JINHO"))
            .andExpect(jsonPath("$.nickname").value("Mentos"))
            .andExpect(jsonPath("$.roles[0].role").value("USER"));
    }

    @Test
    void 회원가입_이미_존재하는_아이디() throws Exception {
        // 같은 아이디로 한 번 더 요청
        String signupRequest = """
            {
              "username": "JINHO",
              "password": "12341234",
              "nickname": "Mentos"
            }
            """;
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
            .andExpect(status().isOk());

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"));
    }

    @Test
    void 로그인_성공() throws Exception {
        String signupRequest = """
        {
          "username": "JINHO",
          "password": "12341234",
          "nickname": "Mentos"
        }
        """;
        mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(signupRequest));

        String loginRequest = """
        {
          "username": "JINHO",
          "password": "12341234"
        }
        """;
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        String loginRequest = """
        {
          "username": "JINHO",
          "password": "wrongpassword"
        }
        """;
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void 관리자_권한_부여_성공() throws Exception {
        // 1. 관리자 회원가입 (관리자 암호 포함)
        String adminSignupRequest = """
        {
          "username": "ADMIN",
          "password": "admin1234",
          "nickname": "관리자",
          "adminKey": "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC"
        }
        """;
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminSignupRequest))
            .andExpect(status().isOk());

        // 2. 관리자 로그인 → 토큰 추출
        String adminLoginRequest = """
        {
          "username": "ADMIN",
          "password": "admin1234"
        }
        """;
        MvcResult loginResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adminLoginRequest))
            .andExpect(status().isOk())
            .andReturn();

        String adminToken = // JSON에서 token 추출
            new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        // 3. 일반 유저 회원가입
        String userSignupRequest = """
        {
          "username": "JINHO",
          "password": "12341234",
          "nickname": "Mentos"
        }
        """;
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignupRequest))
            .andExpect(status().isOk());

        // 4. 일반 유저의 userId를 조회 (이 예시는 userId가 2L이라고 가정)
        Long userId = 4L;

        // 5. PATCH 요청으로 관리자 권한 부여
        mockMvc.perform(patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roles[0].role").value("ADMIN"));
    }

    @Test
    void 권한없는_유저가_관리자권한_부여요청_실패() throws Exception {
        // 1. 일반 유저 회원가입 + 로그인
        String userSignupRequest = """
        {
          "username": "USER1",
          "password": "12341234",
          "nickname": "멤버"
        }
        """;
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignupRequest))
            .andExpect(status().isOk());

        String userLoginRequest = """
        {
          "username": "USER1",
          "password": "12341234"
        }
        """;
        MvcResult loginResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userLoginRequest))
            .andExpect(status().isOk())
            .andReturn();

        String userToken = new com.fasterxml.jackson.databind.ObjectMapper()
            .readTree(loginResult.getResponse().getContentAsString())
            .get("token").asText();

        // 2. 관리자 권한 부여 시도 (실패해야 함)
        Long targetUserId = 1L; // 실제로 회원가입된 userId로 바꿔주세요

        mockMvc.perform(patch("/admin/users/{userId}/roles", targetUserId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
            .andExpect(jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
    }
}

