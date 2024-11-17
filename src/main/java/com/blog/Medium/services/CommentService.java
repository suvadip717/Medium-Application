package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.Comment;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogService blogService;

    public Comment addComment(ObjectId blogId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }

        BlogEntry blog = blogService.getIdBlog(blogId);
        Comment comment = new Comment(null, blogId, blog.getTitle(), user.getId(), user.getUsername(), user.getAvater(),
                content, LocalDateTime.now());
        blog.setComments(blog.getComments() + 1);
        blogService.saveBlogEntry(blog);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByBlog(ObjectId blogId) {
        return commentRepository.findByBlogId(blogId);
    }

    public Long getCommentsCount(ObjectId blogId) {
        return (long) commentRepository.findByBlogId(blogId).size();
    }

    public void deleteComment(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }

        BlogEntry blog = blogService.getIdBlog(blogId);
        commentRepository.deleteByBlogIdAndUserId(blogId, user.getId());
        blog.setComments(blog.getComments() - 1);
        blogService.saveBlogEntry(blog);
    }
}
