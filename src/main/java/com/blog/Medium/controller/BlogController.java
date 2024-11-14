package com.blog.Medium.controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        String message = blogService.deleteBlog(id);
        return message;
    }

    @PutMapping("/update-blog/{id}")
    public BlogEntry updateBlog(@RequestParam String title, @RequestParam String subTitle, @RequestParam String content, @RequestParam MultipartFile blogImage, @PathVariable ObjectId id) throws IOException{
        BlogEntry blog = new BlogEntry();
        blog.setTitle(title);
        blog.setSubTitle(subTitle);
        blog.setContent(content);
        BlogEntry updateBlog = blogService.updateBlog(blog, id, blogImage);
        return updateBlog;
    }
}
