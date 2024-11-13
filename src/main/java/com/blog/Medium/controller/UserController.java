package com.blog.Medium.controller;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Medium.model.User;
import com.blog.Medium.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable ObjectId id){
        return userService.getIdUser(id);
    }

    @PutMapping("/update-user/{id}")
    public User updateUser(@PathVariable ObjectId id, @RequestBody User user){
        User newUser = userService.updateUser(id, user);
        return newUser;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable ObjectId id){
        userService.deleteUser(id);
        return "Delete user sucessfully";
    }

}
