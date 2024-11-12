package com.blog.Medium.services;

import java.util.Arrays;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.User;
import com.blog.Medium.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User addUser(User user){
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
        return user;
    }

    public User updateUser(ObjectId id, User user){
        User oldUser = userRepository.findById(id).orElse(new User());
        if(oldUser != null){
            oldUser.setUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(user.getPassword());
            userRepository.save(oldUser);
        }
        return oldUser;
    }

    public Optional<User> getIdUser(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteUser(ObjectId id){
        userRepository.deleteById(id);
    }
}
