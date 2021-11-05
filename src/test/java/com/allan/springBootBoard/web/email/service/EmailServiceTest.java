package com.allan.springBootBoard.web.email.service;

import com.allan.springBootBoard.web.email.model.MailDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender javaMailSender;

    @Test
    public void 메일_발송_테스트() throws Exception {
        //given
        MailDTO TEST_MAIL_DTO = createMailDTO();

        //when
        emailService.mailSend(TEST_MAIL_DTO);

        //then
        verify(javaMailSender, atLeastOnce()).send(any(SimpleMailMessage.class));
    }

    private MailDTO createMailDTO() {
        return MailDTO.builder()
                .address("test@testMail.com")
                .title("testTitle")
                .message("testMessage")
                .build();
    }
}
