package com.blog.Medium.services;

import java.util.Arrays;
import java.util.List;

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
            user.setAvater("https://res.cloudinary.com/drgvpceli/image/upload/v1731496806/kjodl2qpotsrrlfymxfb.jpg");
            userRepository.save(user);
            emailService.sendVerificationEmail(user);
            return user;
        } catch (Exception e) {
            throw new ELException("Error occurred while creating the user", e);
        }
    }
}
