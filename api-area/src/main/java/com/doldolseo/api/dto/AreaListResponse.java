package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AreaListResponse {
        private List<AreaRequest> areaList;
        private Integer startBlockPage;
        private Integer endBlockPage;
        private Integer totalPages;
}
