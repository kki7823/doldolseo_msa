package com.doldolseo.api.area_rest;

import com.doldolseo.api.dto.AreaDTO;
import com.doldolseo.api.entity.Area;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AreaService {
    @Autowired
    private AreaRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public AreaDTO getArea(String name) {
        Area entity = repository.findFirstByName(name);
        AreaDTO area = modelMapper.map(entity, AreaDTO.class);
        return area;
    }

    public Page<AreaDTO> getAreaList(AreaDTO dto, Pageable pageable) {
        Page<Area> entityPage;
        if (dto.getContentType() == null) {
            entityPage = repository.findBySigungu(dto.getSigungu(), pageable);
        } else {
            entityPage = repository.findBySigunguAndContentType(dto.getSigungu(), dto.getContentType(), pageable);
        }

        //Page<Area> to Page<AreaDTO>
        Page<AreaDTO> areaList = modelMapper.map(entityPage, new TypeToken<Page<AreaDTO>>() {
        }.getType());
        return areaList;
    }

    public Page<AreaDTO> getAreaListBySearch(AreaDTO dto, Pageable pageable) {
        Page<Area> entityPage = repository.findBySigunguAndNameContaining(dto.getSigungu(), dto.getSearchKeyword(), pageable);

        Page<AreaDTO> areaList = modelMapper.map(entityPage, new TypeToken<Page<AreaDTO>>() {
        }.getType());
        return areaList;
    }
}
