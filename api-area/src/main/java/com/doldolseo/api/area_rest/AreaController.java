package com.doldolseo.api.area_rest;

import com.doldolseo.api.dto.AreaDTO;
import com.doldolseo.api.dto.AreaPageDTO;
import com.doldolseo.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaController {
    @Autowired
    private AreaService service;

    @GetMapping(value = "/area")
    public ResponseEntity<AreaPageDTO> areaList(AreaDTO dto,
                                                @PageableDefault(page = 0, size = 12) Pageable pageable) {
        Page<AreaDTO> areaList;
        if (dto.getSearchKeyword() == null) {
            areaList = service.getAreaList(dto, pageable);
        } else {
            areaList = service.getAreaListBySearch(dto, pageable);
        }

        PagingUtil pagingUtil = new PagingUtil(10, areaList);

        AreaPageDTO areaPageDTO = new AreaPageDTO();
        areaPageDTO.setAreaList(areaList.getContent());
        areaPageDTO.setStartBlockPage(pagingUtil.startBlockPage);
        areaPageDTO.setEndBlockPage(pagingUtil.endBlockPage);
        areaPageDTO.setTotalPages(pagingUtil.totalPages);

        return ResponseEntity.status(HttpStatus.OK).body(areaPageDTO);
    }

    @PostMapping(value = "/area/{name}")
    public ResponseEntity<AreaDTO> areaDetail(@PathVariable("name") String name) {
        System.out.println("name: "+name);
        AreaDTO area = service.getArea(name);
        System.out.println("dto: "+area.toString());
        return ResponseEntity.status(HttpStatus.OK).body(area);
    }

}
