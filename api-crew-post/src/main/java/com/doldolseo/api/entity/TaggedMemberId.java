package com.doldolseo.api.entity;

import lombok.*;

import java.io.Serializable;

/*
    태그된 멤버 복합키 (포스팅 번호 , 멤버 Id)
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TaggedMemberId implements Serializable {
    private Long crewPost;
    private String memberId;
}
