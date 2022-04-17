package com.doldolseo.api.repository;

import com.doldolseo.api.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, String> {
    Area findFirstByName(String name);

    @Query("select a from Area a where (a.sigungu=?1 or a.contentType=?2) and a.name like %?3%")
    Page<Area> findBySigunguOrContentTypeAndSearchName(Integer sigungu, Integer contentType, String name, Pageable pageable);
}
