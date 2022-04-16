package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CrewPostPageDTO {
    private List<CrewPostDTO> crewPosts;
    private int startBlockPage;
    private int endBlockPage;
    private int totalPages;
}
