package study.moum.auth.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.email.service.EmailService;
import study.moum.global.error.exception.AlreadyVerifiedEmailException;
import study.moum.redis.util.RedisUtil;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이미 가입된 이메일로 인증 시도 시 실패 테스트")
    void SendMailFail_AlreadyVerifiedEmail() {
        // Given
        String email = "duplicate@example.com";

        when(memberRepository.existsByEmail(email)).thenReturn(true); // 중복된 이메일 체크 걸림

        // Then
        assertThrows(AlreadyVerifiedEmailException.class, () -> {
            emailService.sendCertificationMail(email);
        });

        verify(memberRepository).existsByEmail(email);
        verifyNoInteractions(javaMailSender); // 메일 발송이 호출되지 않아야 함
    }

    @Test
    @DisplayName("이메일 인증 코드 발송 성공 테스트")
    void SendMailSuccess() throws Exception {
        // Given
        String email = "test@example.com";
        String verificationCode = "123456";

        when(memberRepository.existsByEmail(email)).thenReturn(false);  // 중복 이메일 아님
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendCertificationMail(email);

        // Then
        verify(memberRepository).existsByEmail(email);  // 중복 체크 호출 확인
        verify(javaMailSender).send(any(MimeMessage.class));  // 메일 전송 확인
//        verify(redisUtil).setDataExpire(email, verificationCode, 60L);  // Redis 저장 확인 -> 왜 진짜 코드가 날아가지?
    }

    @Test
    @DisplayName("메일 전송 중 예외 발생 테스트")
    void SendMailFail_RuntimeException() throws Exception {
        // Given
        String email = "test@example.com";

        when(memberRepository.existsByEmail(email)).thenReturn(false);  // 중복 이메일 아님
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Mail sending failed")).when(javaMailSender).send(any(MimeMessage.class));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            emailService.sendCertificationMail(email);
        });

        verify(memberRepository).existsByEmail(email);  // 중복 체크 호출 확인
        verify(javaMailSender).send(any(MimeMessage.class));  // 메일 전송 시도 확인
    }
}
