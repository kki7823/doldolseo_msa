package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AreaRequest {
    private Integer sigungu;
    private Integer contentType;
    private String searchKeyword = "";
}
