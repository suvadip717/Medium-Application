package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.Like;
import com.blog.Medium.model.User;

@Service
public class LikeService {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    public BlogEntry addLike(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        Optional<BlogEntry> blogOptional = blogService.getIdBlog(blogId);
        if (user.isVerified()) {
            if (blogOptional.isPresent()) {
                BlogEntry blog = blogOptional.get();
                boolean alreadyLiked = blog.getLikes().stream().anyMatch(like -> like.getUserId().equals(user.getId()));
                if (!alreadyLiked) {
                    blog.getLikes().add(new Like(user.getId(), LocalDateTime.now()));
                    return blogService.saveBlogEntry(blog);
                }
                throw new RuntimeException("User has already liked this post");
            }
            throw new RuntimeException("Blog not found");
        }
        throw new RuntimeException("User not verified");
    }

    public void removeLike(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        Optional<BlogEntry> blogOptional = blogService.getIdBlog(blogId);
        if (blogOptional.isPresent()) {
            BlogEntry blog = blogOptional.get();
            blog.getLikes().removeIf(like -> like.getUserId().equals(user.getId()));
            blogService.saveBlogEntry(blog);
        } else {
            throw new RuntimeException("Blog not found");
        }
    }

    public long getLikesCount(ObjectId blogId) {
        return blogService.getIdBlog(blogId)
                .map(blog -> blog.getLikes().size())
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }
}
