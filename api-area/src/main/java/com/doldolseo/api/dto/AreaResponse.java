package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AreaResponse {
    private String name;
    private String address;
    private Integer sigungu;
    private Integer zipcode;
    private String tel;
    private Float x;
    private Float y;
    private String image1;
    private String image2;
    private Integer contentType;
    private Long contentId;
}
