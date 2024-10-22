package com.payme.emailSerivice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.mailSender = javaMailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yohannesm8814@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public static void main(String[] args) {
        EmailService emailService = new EmailService(new JavaMailSenderImpl());
        emailService.sendSimpleMessage(
                "yohannesm8815@gmail.com",
                "test",
                "this is test, hopefully it works. "
        );
    }
}
