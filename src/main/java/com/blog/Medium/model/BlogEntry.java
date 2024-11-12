package com.blog.Medium.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "blogs_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogEntry {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    @DBRef
    private User auther;
    private LocalDateTime date;
}
