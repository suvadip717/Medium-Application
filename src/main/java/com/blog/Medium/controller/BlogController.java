package com.blog.Medium.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.services.BlogService;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    
    @PostMapping("/create-blog")
    public ResponseEntity<BlogEntry> createBlog(@RequestBody BlogEntry blog){
        try {
            BlogEntry newBlog = blogService.addBlog(blog);
            return new ResponseEntity<>(newBlog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteBlog(@PathVariable ObjectId id){
        blogService.deleteBlog(id);
        return "Blog delete sucessfully";
    }

    @PutMapping("/update-blog/{id}")
    public BlogEntry updateBlog(@RequestBody BlogEntry blog, @PathVariable ObjectId id){
        BlogEntry newBlog = blogService.updateBlog(blog, id);
        return newBlog;
    }
}
