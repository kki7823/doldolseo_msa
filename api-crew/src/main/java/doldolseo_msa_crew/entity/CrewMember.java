package doldolseo_msa_crew.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREW_MEMBER_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CrewMemberId.class)
public class CrewMember implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "CREW_NO")
    private Crew crew;

    @Id
    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "MEMBER_ROLE")
    private String memberRole;

    @Column(name = "J_DATE")
    private LocalDateTime jDate;
}