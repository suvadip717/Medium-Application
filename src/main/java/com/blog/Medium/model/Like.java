package com.blog.Medium.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Likes_entries")
public class Like {
    @Id
    private ObjectId id;
    private ObjectId blogId;
    private ObjectId userId;
    private LocalDateTime likeAt;
}
