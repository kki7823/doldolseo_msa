package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReviewCommentsResponse {
    private List<ReviewCommentResponse> comments;
    private Long numOfComments;
}
