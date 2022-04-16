package doldolseo_msa_crew_board.repository;

import doldolseo_msa_crew_board.entity.TaggedMember;
import doldolseo_msa_crew_board.entity.TaggedMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaggedMemberRepository extends JpaRepository<TaggedMember, TaggedMemberId> {
    List<TaggedMember> findAllByCrewPost_CrewPostNo(Long crewPostNo);
}
