package com.blog.Medium.controller;

import java.io.IOException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.Medium.model.User;
import com.blog.Medium.services.OtpService;
import com.blog.Medium.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    OtpService otpService;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable ObjectId id) {
        return userService.getIdUser(id);
    }

    @PutMapping("/update-user")
    public User updateUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam("avatar") MultipartFile avatarFile) throws IOException {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        User newUser= userService.updateUser(user, avatarFile);
        return newUser;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable ObjectId id) {
        userService.delUserId(id);
        return "Delete user sucessfully";
    }

    @PostMapping("/verify-user")
    public ResponseEntity<String> verifyOtp(@RequestParam String otp) {
        boolean isVerified = otpService.verifyOtp(otp);
        if (isVerified) {
            return ResponseEntity.ok("Account verified successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP");
    }

    @GetMapping("/sent-otp")
    public ResponseEntity<String> sentOtp(){
        String message = userService.sentOtp();
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
