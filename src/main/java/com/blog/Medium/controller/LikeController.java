package com.blog.Medium.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.services.LikeService;

@RestController
@RequestMapping("/blog/{blogId}/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping
    public BlogEntry addLikes(@PathVariable ObjectId blogId){
        return likeService.addLike(blogId);
    }

    @DeleteMapping
    public void removeLike(@PathVariable ObjectId blogId){
        likeService.removeLike(blogId);
    }

    @GetMapping("/count")
    public long getLikesCount(@PathVariable ObjectId blogId){
        return likeService.getLikesCount(blogId);
    }
}
