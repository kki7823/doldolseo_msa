package com.doldolseo.api.dto;

import com.doldolseo.api.entity.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReviewCommentRequest {
    private Long commentNo;
    private Review review;
    private String id;
    private String content;
    private LocalDateTime wDate;
}
