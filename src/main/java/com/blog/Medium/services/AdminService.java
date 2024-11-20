package com.blog.Medium.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.Medium.model.User;
import com.blog.Medium.repository.BlogRepository;
import com.blog.Medium.repository.UserRepository;

import jakarta.el.ELException;

@Service
public class AdminService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SendEmailService emailService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @SuppressWarnings("unused")
    public String deletUser(ObjectId userId){
        Optional<User> OptionalUser = userRepository.findById(userId);
        if(OptionalUser != null){
            User user = OptionalUser.get();
            String username = user.getUsername().toString();
            userRepository.deleteById(userId);
            return username + " is delete successfully";
        }
        return "User not found";
    }

    public String delBlogId(ObjectId blogId){
        blogRepository.deleteById(blogId);
        return "Delete blog Successfully";
    }

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public User saveAdminUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER", "ADMIN"));
            user.setAvatar("https://res.cloudinary.com/drgvpceli/image/upload/v1731496806/kjodl2qpotsrrlfymxfb.jpg");
            userRepository.save(user);
            emailService.sendVerificationEmail(user);
            return user;
        } catch (Exception e) {
            throw new ELException("Error occurred while creating the user", e);
        }
    }
}
