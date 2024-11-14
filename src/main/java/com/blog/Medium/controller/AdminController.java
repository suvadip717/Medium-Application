package com.blog.Medium.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Medium.model.User;
import com.blog.Medium.services.AdminService;
import com.blog.Medium.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @PostMapping("/create-admin")
    public User createAdmin(@RequestBody User user){
        User newUser = adminService.saveAdminUser(user);
        return newUser;
    }

    @GetMapping("/all-users")
    public List<User> allUsers(){
        return adminService.allUsers();
    }

    @DeleteMapping("/del-user/{userId}")
    public String deleteUser(@PathVariable ObjectId userId){
        String message = userService.delUserId(userId);
        return message;
    }

    @DeleteMapping("/del-blog/{blogId}")
    public String deleteBlog(@PathVariable ObjectId blogId) {
      String message = adminService.delBlogId(blogId);
      return message;
    }
}
