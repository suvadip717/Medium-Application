package com.blog.Medium.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.blog.Medium.model.Like;

@Repository
public interface LikeRepository extends MongoRepository<Like, ObjectId> {
    List<Like> findByBlogId(ObjectId blogId);
    void deleteByBlogIdAndUserId(ObjectId blogId, ObjectId userId);
}
