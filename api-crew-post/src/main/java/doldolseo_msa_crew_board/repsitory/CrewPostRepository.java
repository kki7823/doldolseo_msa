package doldolseo_msa_crew_board.repsitory;

import doldolseo_msa_crew_board.entity.CrewPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewPostRepository extends JpaRepository<CrewPost, Long> {
    Page<CrewPost> findAllByCrewNo(Long crewNo, Pageable pageable);

    Page<CrewPost> findAllByCategory(int category, Pageable pageable);

    Page<CrewPost> findAllByWriterId(String writerId, Pageable pageable);

    CrewPost findAllByCrewPostNo(Long crewPostNo);

    @Query("select p.writerId from CrewPost p where p.crewPostNo = ?1")
    String getWriterId(Long crewPostNo);

    void deleteAllByWriterId(String memberId);

    void deleteAllByCrewNo(Long crewNo);
}
