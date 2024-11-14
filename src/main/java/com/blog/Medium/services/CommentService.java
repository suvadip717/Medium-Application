package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.Comment;
import com.blog.Medium.model.User;

@Service
public class CommentService {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    public BlogEntry addComment(ObjectId blogId, Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        if (user.isVerified()) {
            Optional<BlogEntry> blogOptional = blogService.getIdBlog(blogId);
            if (blogOptional.isPresent()) {
                BlogEntry blog = blogOptional.get();
                blog.getComments().add(new Comment(user.getId(), comment.getContent(), LocalDateTime.now()));
                return blogService.saveBlogEntry(blog);
            }
            throw new RuntimeException("Blog not found");
        } else {
            throw new RuntimeException("User is not verified");
        }
    }

    public List<Comment> getCommentsByPost(ObjectId blogId) {
        return blogService.getIdBlog(blogId)
                .map(BlogEntry::getComments)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    public void deleteComment(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if (!user.isVerified()) {
            throw new RuntimeException("User is not verified");
        }

        Optional<BlogEntry> blogOptional = blogService.getIdBlog(blogId);
        if (blogOptional.isPresent()) {
            BlogEntry blog = blogOptional.get();
            boolean commentRemoved = blog.getComments().removeIf(comment -> comment.getUserId().equals(user.getId()));
            
            if(commentRemoved){
                blogService.saveBlogEntry(blog);
            }else{
                throw new RuntimeException("Comment not found for this user");
            }
        } else {
            throw new RuntimeException("Blog not found");
        }
    }
}
