package com.doldolseo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/*
 * 후기게시판 DTO
 *
 * @Author 김경일
 * @Date 2021/08/06
 */

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
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
