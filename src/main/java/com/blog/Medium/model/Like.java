package com.blog.Medium.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Like {
    private ObjectId userId;
    private LocalDateTime likeAt;
}
