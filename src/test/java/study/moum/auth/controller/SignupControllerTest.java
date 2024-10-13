package study.moum.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import study.moum.auth.dto.MemberDto;
import study.moum.auth.service.SignupService;
import study.moum.global.error.ErrorCode;
import study.moum.global.error.exception.DuplicateUsernameException;
import study.moum.global.response.ResponseCode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignupController.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupService signupService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("회원가입 - 성공")
    @WithMockUser(username = "testUser")
    void signupMember_ShouldReturnSuccess() throws Exception {
        // Given
        MemberDto.Request memberRequestDto = MemberDto.Request.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .email("test@test.com")
                .build();

        // When
        doNothing().when(signupService).signupMember(any(MemberDto.Request.class));

        // Then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .with(csrf())) // CSRF 토큰 추가 -> 이거 있어야됨
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.REGISTER_SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(memberRequestDto.getUsername()));
    }

    @Test
    @DisplayName("회원가입 - 이미 존재하는 사용자 에러")
    @WithMockUser(username = "testUser")
    void signupMember_ShouldReturnConflict_WhenUsernameAlreadyExists() throws Exception {
        // Given
        MemberDto.Request memberRequestDto = MemberDto.Request.builder()
                .id(1)
                .username("duplUser")
                .password("password123")
                .email("test@test.com")
                .build();

        // When
        // 이미 존재하는 username인 경우 DuplicateUsernameException 발생
        doThrow(new DuplicateUsernameException())
                .when(signupService).signupMember(any(MemberDto.Request.class));

        // Then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NAME_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NAME_ALREADY_EXISTS.getMessage()));
    }
}