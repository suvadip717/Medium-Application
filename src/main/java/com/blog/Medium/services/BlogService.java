package com.blog.Medium.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.blog.Medium.config.CloudinaryConfig;
import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.BlogRepository;

import jakarta.el.ELException;

@Service
public class BlogService {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserService userService;

    @Autowired 
    CloudinaryConfig cloudinaryConfig;

    @Transactional
    public BlogEntry addBlog(BlogEntry blog) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        if (user.isVerified()) {
            try {
                blog.setDate(LocalDateTime.now());
                blog.setAuther(user.getUsername());
                blog.setBlogImage("https://res.cloudinary.com/drgvpceli/image/upload/v1731564102/strjreh0tcxkrmiv2pjd.jpg");
                BlogEntry saveBlog = blogRepository.save(blog);
                user.getBlogs().add(saveBlog);
                userService.saveUser(user);
                return saveBlog;
            } catch (Exception e) {
                throw new ELException("Get error while add new blog");
            }
        }
        throw new RuntimeException("User not verified, please verify your account");
    }

    public List<BlogEntry> allBlogs() {
        return blogRepository.findAll();
    }

    public String deleteBlog(ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        Optional<BlogEntry> blogEntry = blogRepository.findById(id);
        BlogEntry blog = blogEntry.get();
        
        if (user.isVerified() && blog.getAuther().equals(username)) {
            blogRepository.deleteById(id);
            return "Blog Delete successfully";
        }else{
            return "User is not verified";
        }
    }

    public BlogEntry updateBlog(BlogEntry blog, ObjectId id, MultipartFile blogImage) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        BlogEntry oldBlog = blogRepository.findById(id).orElse(new BlogEntry());

        if (user.isVerified() && username.equals(oldBlog.getAuther())) { 
            Map data = this.cloudinaryConfig.cloudinary().uploader().upload(blogImage.getBytes(), Map.of());
            if (oldBlog != null) {
                oldBlog.setTitle(blog.getTitle());
                oldBlog.setContent(blog.getContent());
                oldBlog.setSubTitle(blog.getSubTitle());
                oldBlog.setBlogImage(data.get("url").toString());
                blogRepository.save(oldBlog);
            }
            return oldBlog;
        }
        throw new RuntimeException("User is not verified");
    }

    public Optional<BlogEntry> getIdBlog(ObjectId id) {
        return blogRepository.findById(id);
    }

    public BlogEntry saveBlogEntry(BlogEntry blog) {
        return blogRepository.save(blog);
    }
}
