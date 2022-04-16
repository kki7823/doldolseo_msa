package doldolseo_msa_crew.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CrewAndCrewMemberDTO {
    private CrewDTO crew;
    private List<CrewMemberDTO> crewMember;
}
