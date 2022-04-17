package com.doldolseo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewRequest {
    private Long reviewNo;
    private String id;
    private String title;
    private String content;
    private LocalDateTime wDate;
    private int hit;
    private int areaNo;
    private String imageUUID;
    private char isCourseUploaded;
}
