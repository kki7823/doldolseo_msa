package com.doldolseo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CrewPostCommentsDTO {
    private List<CrewPostCommentDTO> comments;
    private Long numOfComments;
}
