package com.doldolseo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.doldolseo.api.entity.CrewPost;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaggedMemberDTO {
    private CrewPost crewPost;
    private String memberId;
}
