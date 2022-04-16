package com.doldolseo.api.dto;

import com.doldolseo.api.entity.CrewPost;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CrewPostCommentDTO {
    private Long commentNo;
    private CrewPost crewPost;
    private String id;
    private String content;
    private LocalDateTime wDate;
}
