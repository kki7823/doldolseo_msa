package com.doldolseo.api;

import com.doldolseo.api.entity.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, String> {
    Area findFirstByName(String name);

    Page<Area> findBySigunguAndContentType(Integer sigungu, Integer contentType, Pageable pageable);

    Page<Area> findBySigungu(Integer sigungu, Pageable pageable);

    Page<Area> findBySigunguAndNameContaining(Integer sigungu, String name, Pageable pageable);

    Page<Area> findBySigunguOrContentTypeOrNameContaining(Integer sigungu, Integer contentType, String name, Pageable pageable);
}
