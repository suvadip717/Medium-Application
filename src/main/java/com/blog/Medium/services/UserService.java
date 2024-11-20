package com.blog.Medium.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blog.Medium.config.CloudinaryConfig;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.UserRepository;

import jakarta.el.ELException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SendEmailService emailService;

    @Autowired
    CloudinaryConfig cloudinaryConfig;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public User saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            user.setAvatar("https://res.cloudinary.com/drgvpceli/image/upload/v1731496806/kjodl2qpotsrrlfymxfb.jpg");
            userRepository.save(user);
            emailService.sendVerificationEmail(user);
            return user;
        } catch (Exception e) {
            throw new ELException("Error occurred while creating the user", e);
        }
    }

    public String sentOtp() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            if(!user.isVerified()){
                emailService.sendVerificationEmail(user);
            }
            else{
                return "User already verified";
            }
            return "Sending OTP Successfully";
        } catch (Exception e) {
            throw new ELException("Error sending otp", e);
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user, MultipartFile avatarFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User oldUser = userRepository.findByUsername(username);
        String email = oldUser.getEmail();
        if (oldUser != null) {
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            oldUser.setEmail(user.getEmail());
            if(!oldUser.getEmail().equals(email)){
                emailService.sendVerificationEmail(oldUser);
            }
            try {
                Map data = this.cloudinaryConfig.cloudinary().uploader().upload(avatarFile.getBytes(), Map.of());
                oldUser.setAvatar(data.get("url").toString());
            } catch (Exception e) {
                throw new ELException("Upload avater image failed");
            }
            userRepository.save(oldUser);
        }
        return oldUser;
    }

    public User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        return user;
    }

    public String delUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        userRepository.deleteById(user.getId());
        return "Delete user Successfully";
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
