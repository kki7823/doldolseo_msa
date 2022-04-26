package com.doldolseo.api.controller;

import com.doldolseo.api.dto.AreaResponse;
import com.doldolseo.api.service.AreaService;
import com.doldolseo.api.dto.AreaRequest;
import com.doldolseo.api.dto.AreaListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AreaController {
    @Autowired
    private AreaService service;

    @GetMapping(value = "/area")
    public ResponseEntity<AreaListResponse> getAreaList(AreaRequest request,
                                                        @PageableDefault(page = 0, size = 12) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAreaList(request, pageable));
    }

    @PostMapping(value = "/area/{name}")
    public ResponseEntity<AreaResponse> areaDetail(@PathVariable("name") String name){
        return ResponseEntity.status(HttpStatus.OK).body(service.getArea(name));
    }

}
