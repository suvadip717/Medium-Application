package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.repository.BlogRepository;

import jakarta.el.ELException;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;

    // @Autowired
    // UserService userService;

    @Transactional
    public BlogEntry addBlog(BlogEntry blog, String username) {
        try {
            // User user = userService.findByUserName(username);
            blog.setDate(LocalDateTime.now());
            BlogEntry saveBlog = blogRepository.save(blog);
            return saveBlog;
        } catch (Exception e) {
            throw new ELException("Get error while add new blog");
        }
    }

    public List<BlogEntry> allBlogs() {
        return blogRepository.findAll();
    }

    public void deleteBlog(ObjectId id) {
        blogRepository.deleteById(id);
    }

    public BlogEntry updateBlog(BlogEntry blog, ObjectId id) {
        BlogEntry oldBlog = blogRepository.findById(id).orElse(new BlogEntry());
        if (oldBlog != null) {
            oldBlog.setTitle(blog.getTitle());
            oldBlog.setContent(blog.getContent());
            blogRepository.save(oldBlog);
        }
        return oldBlog;
    }

    public Optional<BlogEntry> getIdBlog(ObjectId id) {
        return blogRepository.findById(id);
    }
}
