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

import com.blog.Medium.model.Comment;
import com.blog.Medium.services.CommentService;

@RestController
@RequestMapping("/blog/{blogId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@PathVariable ObjectId blogId, @RequestBody String content){
        return commentService.addComment(blogId, content);
    }
    
    @GetMapping
    public List<Comment> getComment(@PathVariable ObjectId blogId){
        return commentService.getCommentsByBlog(blogId);
    }

    @GetMapping("/count")
    public Long getCount(@PathVariable ObjectId blogId){
        return commentService.getCommentsCount(blogId);
    }

    @DeleteMapping
    public void removeComment(@PathVariable ObjectId blogId){
        commentService.deleteComment(blogId);
    }
}
