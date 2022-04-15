package com.doldolseo.area_service.service;

import com.doldolseo.area_service.dto.AreaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AreaService {
    public AreaDTO getArea(String name);

    public Page<AreaDTO> getAreaList(AreaDTO dto, Pageable pageable);

    public Page<AreaDTO> getAreaListBySearch(AreaDTO dto, Pageable pageable);
}
