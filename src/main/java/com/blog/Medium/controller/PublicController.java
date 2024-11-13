package com.blog.Medium.controller;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.User;
import com.blog.Medium.services.BlogService;
import com.blog.Medium.services.UserService;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Ok";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        User addUser = userService.saveNewUser(user);
        return addUser;
    }

    @GetMapping("/all-blogs")
    public List<BlogEntry> allBlogs(){
        return blogService.allBlogs();
    }

    @GetMapping("{id}")
    public Optional<BlogEntry> getBlog(@PathVariable ObjectId id){
        return blogService.getIdBlog(id);
    }
}
