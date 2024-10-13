package study.moum.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import study.moum.email.controller.EmailController;
import study.moum.email.dto.EmailDto;
import study.moum.email.service.EmailService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("이메일 인증 코드 발송 성공 테스트")
    void EmailSendCodeSuccess() throws Exception {
        // Given
        String validEmail = "test@example.com";
        EmailDto.Request request = new EmailDto.Request();
        request.setEmail(validEmail);

        // when
        when(emailService.sendCertificationMail(validEmail)).thenReturn("123456");

        // When & Then
        mockMvc.perform(post("/send-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(emailService).sendCertificationMail(validEmail);
    }

    @Test
    @DisplayName("이메일 형식이 잘못된 경우 실패 테스트")
    void EmailSendFail_InvalidEmailForm() throws Exception {
        // Given
        String invalidEmail = "invalid-email-form";
        EmailDto.Request request = new EmailDto.Request();
        request.setEmail(invalidEmail);

        // When & Then
        mockMvc.perform(post("/send-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일이 비어있는 경우 실패 테스트")
    void EmailSendFail_EmptyEmailForm() throws Exception {
        // Given
        EmailDto.Request request = new EmailDto.Request(); // 이메일이 설정되지 않음

        // When & Then
        mockMvc.perform(post("/send-mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
