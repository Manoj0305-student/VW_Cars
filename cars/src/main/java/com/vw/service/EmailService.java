package com.vw.service;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;import org.springframework.stereotype.Service;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String text){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }}
