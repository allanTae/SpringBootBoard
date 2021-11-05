package com.allan.springBootBoard.web.email.service;

import com.allan.springBootBoard.web.email.model.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // 발신 이메일 주소.
    private static final String FROM_ADDRESS = "delvelopallan@gmail.com";

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public MailDTO createMail(String memberEmail, String memberName, String tempPwd){
        MailDTO mailDTO = MailDTO.builder()
                .address(memberEmail)
                .title(memberName + "님의 FAKEUSER.COM 임시 비밀번호 안내 메일입니다.")
                .message("안녕하세. FAKEUSER.COM 임시 비밀번호 안내 관련 이메일 입니다. " + "[" + memberName +"]" + "님의 임시 비밀번호는 " + tempPwd +" 입니다.")
                .build();
        return mailDTO;
    }

    public void mailSend(MailDTO mailDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setFrom(EmailService.FROM_ADDRESS);
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());

        mailSender.send(message);
    }
}
