package com.blog.Medium.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private ObjectId userId;
    private String content;
    private LocalDateTime createdAt;
}
