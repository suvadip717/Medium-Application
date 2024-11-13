package com.blog.Medium.services;

import java.util.Arrays;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.User;
import com.blog.Medium.repository.UserRepository;

import jakarta.el.ELException;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new ELException("Getting error while creating user");
        }
    }

    public User saveUser(User user) {
        return user;
    }

    public User updateUser(ObjectId id, User user) {
        User oldUser = userRepository.findById(id).orElse(new User());
        if (oldUser != null) {
            oldUser.setUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(oldUser);
        }
        return oldUser;
    }

    public Optional<User> getIdUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUserName(String username){
        return userRepository.findByUsername(username);
    }
}
