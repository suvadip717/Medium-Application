package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.Comment;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(ObjectId blogId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }
      
        Comment comment = new Comment(null,blogId, user.getId(), content, LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBlog(ObjectId blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public void deleteComment(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }

        commentRepository.deleteByBlogIdAndUserId(blogId, user.getId());
    }
}
