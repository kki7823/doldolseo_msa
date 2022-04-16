package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CrewAndAllCrewMemberDTO {
    private CrewDTO crew;
    private List<CrewMemberDTO> crewMember;
    private List<CrewWatingMemberDTO> crewWatingMember;
}
