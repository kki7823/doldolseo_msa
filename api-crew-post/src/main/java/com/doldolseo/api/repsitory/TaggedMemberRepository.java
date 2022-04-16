package com.doldolseo.api.repsitory;

import com.doldolseo.api.entity.TaggedMember;
import com.doldolseo.api.entity.TaggedMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaggedMemberRepository extends JpaRepository<TaggedMember, TaggedMemberId> {
    List<TaggedMember> findAllByCrewPost_CrewPostNo(Long crewPostNo);
}
