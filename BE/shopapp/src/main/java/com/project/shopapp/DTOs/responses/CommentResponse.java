package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private long id;
    @JsonProperty("content")
    private String content;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("product_id")
    private long productId;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public static CommentResponse fromComment(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userName(comment.getUser().getFullName())
                .productId(comment.getProduct().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
