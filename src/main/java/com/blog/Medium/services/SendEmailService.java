package com.blog.Medium.services;

// import java.time.LocalDateTime;
// import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.User;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    OtpService otpService;


    public void sendVerificationEmail(User user){
        String otp = otpService.generateOtp(user);
        sendEmail(user.getEmail(), "OTP Verification",
                "Your OTP is " + otp + ". It will expire in 5 minutes.");
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            sender.send(mail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
