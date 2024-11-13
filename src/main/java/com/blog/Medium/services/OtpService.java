package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.User;

@Service
public class OtpService {
    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private UserService userService;

    public String generateOtp(User user) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        user.setVerified(false);
        userService.saveUser(user);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userService.findByEmail(email);
        if (user != null && otp.equals(user.getOtp()) && LocalDateTime.now().isBefore(user.getOtpExpiry())) {
            user.setVerified(true);
            user.setOtp(null); 
            user.setOtpExpiry(null);
            userService.saveUser(user);
            return true;
        }
        return false;
    }
}