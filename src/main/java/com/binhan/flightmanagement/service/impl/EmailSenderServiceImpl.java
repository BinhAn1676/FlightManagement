package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;
    private Random random = new Random();
    private Integer codeNumber = random.nextInt(99999 - 10000 + 1) + 10000;
    @Autowired
    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("gianhi8596@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        String secretCode = "This is your secret number to change password " + codeNumber.toString();
        simpleMailMessage.setText(message + "\n" + secretCode);
        this.mailSender.send(simpleMailMessage);
    }

    @Override
    public boolean checkNumber(Integer numberToCheck) {
        return numberToCheck.equals(codeNumber);
    }
}
