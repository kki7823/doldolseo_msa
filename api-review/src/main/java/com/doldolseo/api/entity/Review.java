package com.doldolseo.api.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
 * 후기게시판 Entity
 *
 * @Author 김경일
 * @Date 2021/08/06
 */

@Entity
@Table(name = "REVIEW_TBL")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_NO")
    private Long reviewNo;

    @Column(name = "ID")
    private String id;

    private String title;
    private String content;

    @Column(name = "W_DATE")
    private LocalDateTime wDate;

    private int hit;

    @Column(name = "AREA_NO")
    private int areaNo;

    @Column(name = "IMAGE_UUID")
    private String imageUUID;

    private char isCourseUploaded;
}
