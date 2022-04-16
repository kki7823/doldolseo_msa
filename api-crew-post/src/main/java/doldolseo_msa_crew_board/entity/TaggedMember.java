package doldolseo_msa_crew_board.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CREW_POST_TAGGED_MEMBER_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
@IdClass(TaggedMemberId.class)
public class TaggedMember implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "CREW_POST_NO")
    private CrewPost crewPost;

    @Id
    @Column(name = "MEMBER_ID")
    private String memberId;
}
