package study.moum.email.service;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.moum.auth.domain.repository.MemberRepository;
import study.moum.email.dto.EmailDto;
import study.moum.global.error.exception.AlreadyVerifiedEmailException;
import study.moum.redis.util.RedisUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;

    @Value("${spring.mail.username}")
    private String sender;

    private MimeMessage createMessage(EmailDto.Request emailDto, String code) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, emailDto.getEmail());
        message.setSubject(" 모음(MOUM) 에서 발송된 인증 번호입니다.");
        message.setText("이메일 인증코드: "+code);
        message.setFrom(sender);

        return message;
    }

    public void sendMail(EmailDto.Request emailDto, String code) throws Exception{

        Boolean isExist = memberRepository.existsByEmail(emailDto.getEmail());
        if(isExist){
            throw new AlreadyVerifiedEmailException();
        }

        try{
            MimeMessage mimeMessage = createMessage(emailDto, code);
            javaMailSender.send(mimeMessage);
        } catch (MailException mailException) {
            throw new RuntimeException("메일 전송에 실패했습니다.");
        }
    }

    public String sendCertificationMail(EmailDto.Request emailDto) throws Exception {
        String code = UUID.randomUUID().toString().substring(0, 6); //랜덤 인증번호 : uuid
        sendMail(emailDto,code);

        redisUtil.setDataExpire(emailDto.getEmail(),code,60*1L); // {key,value} 1분동안 저장.
        return code;
    }
}