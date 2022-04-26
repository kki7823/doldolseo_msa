package com.doldolseo.api.repository;

import com.doldolseo.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findById(String id);
    Optional<Member> findByNickname(String nickName);

    @Query(value = "select m.nickname from Member m where m.id = ?1")
    Optional<Object> findNickNameById(String id);

    void deleteById(String id);
    boolean existsByNickname(String nickName);
}
