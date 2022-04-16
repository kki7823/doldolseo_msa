package com.doldolseo.api.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEOUL_AREA_TBL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Area {
    @Id
    private String name;

    private String address;
    private Integer sigungu;
    private Integer zipcode;
    private String tel;
    private Float x;
    private Float y;
    private String image1;
    private String image2;

    @Column(name = "CONTENTTYPE")
    private Integer contentType;

    @Column(name = "CONTENTID")
    private Long contentId;
}
