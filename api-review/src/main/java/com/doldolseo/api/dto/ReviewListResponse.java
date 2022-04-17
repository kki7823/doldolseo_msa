package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReviewListResponse {
    private List<ReviewRequest> reviewList;
    private int startBlockPage;
    private int endBlockPage;
    private int totalPages;
}
