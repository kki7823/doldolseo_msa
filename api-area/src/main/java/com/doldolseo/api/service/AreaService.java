package com.doldolseo.api.service;

import com.doldolseo.api.dto.AreaRequest;
import com.doldolseo.api.dto.AreaListResponse;
import com.doldolseo.api.dto.AreaResponse;
import com.doldolseo.api.entity.Area;
import com.doldolseo.api.repository.AreaRepository;
import com.doldolseo.common.PagingUtil;
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

    public AreaResponse getArea(String name) {
        Area entity = repository.findFirstByName(name);
        return entityToDto(entity);
    }

    public AreaListResponse getAreaList(AreaRequest request, Pageable pageable) {
        Page<Area> areaPage = repository.findBySigunguOrContentTypeAndSearchName(request.getSigungu(),
                request.getContentType(),
                request.getSearchKeyword(),
                pageable);

        Page<AreaRequest> areaRequestsPage = entityPageToDtoPage(areaPage);
        AreaListResponse response = populatePage(areaRequestsPage);

        return response;
    }

    public AreaListResponse populatePage(Page<AreaRequest> areaRequestsPage) {
        PagingUtil pagingUtil = new PagingUtil(10, areaRequestsPage);

        AreaListResponse response = new AreaListResponse();
        response.setAreaList(areaRequestsPage.getContent());
        response.setStartBlockPage(pagingUtil.startBlockPage);
        response.setEndBlockPage(pagingUtil.endBlockPage);
        response.setTotalPages(pagingUtil.totalPages);
        return response;
    }

    public AreaResponse entityToDto(Area entity) {
        return modelMapper.map(entity, AreaResponse.class);
    }

    public Page<AreaRequest> entityPageToDtoPage(Page<Area> entity) {
        return modelMapper.map(entity, new TypeToken<Page<AreaRequest>>() {
        }.getType());
    }
}
