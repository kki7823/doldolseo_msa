package doldolseo_msa_crew.repository;

import doldolseo_msa_crew.entity.CrewWatingMember;
import doldolseo_msa_crew.entity.CrewWatingMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CrewWatingMemberRepository extends JpaRepository<CrewWatingMember, CrewWatingMemberId> {
    List<CrewWatingMember> findAllByCrew_CrewLeader(String crewLeader);

    Integer countCrewMemberByMemberId(String memberId);
}
