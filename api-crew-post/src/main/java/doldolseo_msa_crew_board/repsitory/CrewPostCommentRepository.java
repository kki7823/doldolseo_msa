package doldolseo_msa_crew_board.repsitory;

import doldolseo_msa_crew_board.entity.CrewPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewPostCommentRepository extends JpaRepository<CrewPostComment, Long> {
    List<CrewPostComment> findAllByCrewPost_CrewPostNo(Long crewPostNo);
    CrewPostComment findByCommentNo(Long commentNo);

    @Query("select c.id from CrewPostComment c where c.commentNo = ?1")
    String getWriterId(Long commentNo);

    Long countCrewPostCommentByCrewPost_CrewPostNo(Long crewPostNo);
}
