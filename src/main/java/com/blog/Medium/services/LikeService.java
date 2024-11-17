package com.blog.Medium.services;

import java.time.LocalDateTime;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.blog.Medium.model.BlogEntry;
import com.blog.Medium.model.Like;
import com.blog.Medium.model.User;
import com.blog.Medium.repository.LikeRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

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

        if (alreadyLiked) {
            throw new RuntimeException("User has already liked this blog");
        }

        BlogEntry blog = blogService.getIdBlog(blogId);
        Like like = new Like(null, blogId, blog.getTitle(), user.getId(), user.getAvater(), user.getUsername(),
                LocalDateTime.now());
        blog.setLikes(blog.getLikes() + 1);
        blogService.saveBlogEntry(blog);
        return likeRepository.save(like);
    }

    public void removeLike(ObjectId blogId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);

        BlogEntry blog = blogService.getIdBlog(blogId);
            likeRepository.deleteByBlogIdAndUserId(blogId, user.getId());
            blog.setLikes(blog.getLikes() - 1);
            blogService.saveBlogEntry(blog);
    }

    public long getLikesCount(ObjectId blogId) {
        return likeRepository.findByBlogId(blogId).size();
    }

    public List<Like> getLikesByBlog(ObjectId blogId) {
        return likeRepository.findByBlogId(blogId);
    }
}
