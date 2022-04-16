package doldolseo_msa_crew_board.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREW_POST_TBL")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CrewPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREW_POST_NO")
    private Long crewPostNo;

    @Column(name = "CREW_NO")
    private Long crewNo;

    @Column(name = "WRITER_ID")
    private String writerId;

    private int category;
    private String title;
    private String content;

    @Column(name = "IMAGE_UUID")
    private String imageUUID;

    @Column(name = "W_DATE")
    private LocalDateTime wDate;

    private int hit;
}
