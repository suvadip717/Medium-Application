package com.blog.Medium.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.blog.Medium.model.BlogEntry;


@Repository
public interface BlogRepository extends MongoRepository<BlogEntry, ObjectId>{
    
}
