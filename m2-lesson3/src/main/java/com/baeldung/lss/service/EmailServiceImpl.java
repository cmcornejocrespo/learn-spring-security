package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(final User user, final String token, final String appUrl) {

        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = appUrl + "/registrationConfirm.html?token=" + token;

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("carlos.cornejo.crespo@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Please click the link below to activate your account\n\n" + confirmationUrl);

        mailSender.send(email);

    }
}
