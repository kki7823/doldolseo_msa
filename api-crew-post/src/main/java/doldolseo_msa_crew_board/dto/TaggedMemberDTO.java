package doldolseo_msa_crew_board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import doldolseo_msa_crew_board.entity.CrewPost;
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
