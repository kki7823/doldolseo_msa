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
public class CrewPostDTO {
    private Long crewPostNo;
    private Long crewNo;
    private String writerId;
    private int category;
    private String title;
    private String content;
    private String imageUUID;
    private LocalDateTime wDate;
    private int hit;
    private String taggedMember;
}
