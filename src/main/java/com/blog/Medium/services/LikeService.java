package com.blog.Medium.services;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.Like;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.LikeRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;

    public Like addLike(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        if (!user.isVerified()) {
            throw new RuntimeException("User not verified");
        }

        boolean alreadyLiked = likeRepository.findByBlogId(blogId)
        .stream()
        .anyMatch(like -> like.getUserId().equals(user.getId()));

        if(alreadyLiked){
            throw new RuntimeException("User has already liked this blog");
        }

        Like like = new Like(null, blogId, user.getId(), LocalDateTime.now());
        return likeRepository.save(like);
    }

    public void removeLike(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        likeRepository.deleteByBlogIdAndUserId(blogId, user.getId());
    }

    public long getLikesCount(ObjectId blogId) {
        return likeRepository.findByBlogId(blogId).size();
    }
}
