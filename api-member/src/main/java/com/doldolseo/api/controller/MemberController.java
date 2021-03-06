package com.doldolseo.api.controller;

import com.doldolseo.api.dto.MemberDTO;
import com.doldolseo.api.dto.MemberLoginDTO;
import com.doldolseo.api.service.MemberAuthService;
import com.doldolseo.api.service.MemberService;
import com.doldolseo.api.utils.UploadProfileUtil;
import com.doldolseo.common.JwtUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberAuthService memberAuthService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UploadProfileUtil uploadProfileUtil;

    @PostMapping("/member")
    public ResponseEntity<MemberDTO> registerMember(MemberDTO dto,
                                                    @RequestParam(required = false) MultipartFile memberImgFile) throws IOException {
        String profileImg = "default_member.png";
        if (memberImgFile != null) {
            if (!memberImgFile.isEmpty()) {
                profileImg = uploadProfileUtil.uploadProfile(memberImgFile, dto);
            }
        }
        dto.setMemberImg(profileImg);
        MemberDTO member = memberService.registMember(dto);

        System.out.println(member.toString());
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PostMapping("/member/check")
    public ResponseEntity<Boolean> check(@RequestParam(required = false) String id,
                                         @RequestParam(required = false) String nickName) {
        Boolean isExist = null;

        if (id != null)
            isExist = memberService.checkMemberId(id);
        else if (nickName != null)
            isExist = memberService.checkMemberNickName(nickName);

        return ResponseEntity.status(HttpStatus.OK).body(isExist);
    }

    @PostMapping("/member/login")
    @ResponseBody
    public ResponseEntity<MemberDTO> loginMember(@RequestBody MemberLoginDTO loginDTO,
                                                 HttpServletResponse response) {
        final MemberDTO memberDTO = memberAuthService.authenticateMember(loginDTO.getId(), loginDTO.getPassword());

        final String token = jwtUtil.generateToken(memberDTO.getId(), memberDTO.getMemberRole());
        jwtUtil.setToken(response, token);

        return ResponseEntity.ok(memberDTO);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable String id) {
        MemberDTO dto = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/member/nickname/{id}")
    public ResponseEntity<String> getNickName(@PathVariable String id) {
        String nickname = memberService.getMemberNickname(id);
        return ResponseEntity.status(HttpStatus.OK).body(nickname);
    }

    @PutMapping("/member/{id}")
    public ResponseEntity<MemberDTO> updateMember(MemberDTO dto,
                                                  @RequestParam(required = false) MultipartFile memberImgFile,
                                                  @PathVariable String id) throws IOException {
        if (memberImgFile != null) {
            if (!memberImgFile.isEmpty()) {
                String profileImg = uploadProfileUtil.uploadProfile(memberImgFile, dto);
                dto.setMemberImg(profileImg);
            }
        }

        memberService.updateMember(id, dto);
        MemberDTO responseDTO = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        MemberDTO dto = memberService.getMember(id);
        if (dto.getMemberRole().equals("CREWLEADER")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("?????? ????????? ????????? ?????????.");
        }

        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/member/images/{id}"
            , produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public byte[] getMemberImages(@PathVariable String id) throws IOException {
        String imageFileName = memberService.getMember(id).getMemberImg();
        String imgPath = System.getProperty("user.dir")
                + "/src/main/resources/static/images/profile/"
                + imageFileName;
        try {
            InputStream in = new FileInputStream(imgPath);
            byte[] imageByteArr = IOUtils.toByteArray(in);
            in.close();
            return imageByteArr;
        } catch (FileNotFoundException e) {
            System.out.println("????????? ???????????? " + e.getMessage());
            return null;
        }
    }

    @PutMapping(value = "/member/role")
    public void updateRole(@RequestParam String id,
                           @RequestParam String action) {
        if (action.equals("PROMOTION")) {
            memberService.updateUserToCrewLeader(id);
            System.out.println("[updateRole] " + id + "??? ????????? CREWLEADER??? ?????? ???????????????.");
        } else if (action.equals("DEMOTION")) {
            memberService.updateCrewLeaderToUser(id);
            System.out.println("[updateRole] " + id + "??? ????????? USER??? ?????? ???????????????.");
        } else
            System.out.println("[updateRole] ????????? ?????? ?????????.");
    }

    @PostMapping(value = "/member/refresh")
    public ResponseEntity<String> refreshToken(@RequestHeader String userId,
                                               HttpServletResponse response) {
        String newRole = memberService.getMember(userId).getMemberRole();
        final String newToken = jwtUtil.generateToken(userId, newRole);
        jwtUtil.setToken(response, newToken);

        return ResponseEntity.status(HttpStatus.OK).body(newRole);
    }
}
