package com.doldolseo.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberDTO {
    private String id;
    private String password;
    private String name;
    private String nickname;
    private String memberImg;
    private String birth;
    private String gender;
    private String email;
    private String phone;
    private LocalDateTime joinDate;
    private String memberRole;
}
