package com.blog.Medium.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.blog.Medium.model.Comment;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, ObjectId>{
    List<Comment> findByBlogId(ObjectId blogId);
    void deleteByBlogIdAndUserId(ObjectId blogId, ObjectId userId);
}
