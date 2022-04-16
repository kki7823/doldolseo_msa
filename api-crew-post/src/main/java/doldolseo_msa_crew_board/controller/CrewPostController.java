package doldolseo_msa_crew_board.controller;

import doldolseo_msa_crew_board.service.CrewPostService;
import doldolseo_msa_crew_board.entity.TaggedMemberId;
import doldolseo_msa_crew_board.dto.CrewPostAndMembersDTO;
import doldolseo_msa_crew_board.dto.CrewPostDTO;
import doldolseo_msa_crew_board.dto.CrewPostPageDTO;
import doldolseo_msa_crew_board.utils.AuthorityUtil;
import doldolseo_msa_crew_board.utils.UploadCrewPostFileUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
public class CrewPostController {
    @Autowired
    CrewPostService service;
    @Autowired
    UploadCrewPostFileUtil fileUtil;
    @Autowired
    AuthorityUtil authorityUtil;

    @PostMapping(value = "/crew/post")
    public ResponseEntity<CrewPostDTO> createPost(CrewPostDTO dtoIn, String taggedMembers) {
        service.saveCrewPost(dtoIn, taggedMembers);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/crew/post")
    public ResponseEntity<CrewPostPageDTO> getCrewPostPage(CrewPostDTO dtoIn,
                                                           @PageableDefault(size = 30, sort = "wDate", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCrewPostPage(dtoIn, pageable));
    }

    @GetMapping(value = "/crew/post/{crewPostNo}")
    public ResponseEntity<CrewPostAndMembersDTO> getPost(@PathVariable(value = "crewPostNo") Long crewPostNo,
                                                         @RequestParam(required = false) Optional<String> doHit) throws Exception {
        if (doHit.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(service.getCrewPostAndMembersAndHit(crewPostNo));
        else
            return ResponseEntity.status(HttpStatus.OK).body(service.getCrewPostAndMembers(crewPostNo));
    }

    @PutMapping(value = "/crew/post/{crewPostNo}")
    public ResponseEntity<?> updatePost(CrewPostDTO dtoIn,
                                        @PathVariable(value = "crewPostNo") Long crewPostNo,
                                        @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, service.getWriterId(crewPostNo))) {
            service.updateCrewPost(dtoIn, crewPostNo);
            return ResponseEntity.status(HttpStatus.OK).body("크루 게시글 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }

    @DeleteMapping(value = "/crew/post/{crewPostNo}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "crewPostNo") Long crewPostNo,
                                        @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, service.getWriterId(crewPostNo))) {
            service.deleteCrewPost(crewPostNo);
            return ResponseEntity.status(HttpStatus.OK).body("크루 게시글 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }

    @DeleteMapping(value = "/crew/post/member/{memberId}")
    public ResponseEntity<?> deletePostByMember(@PathVariable(value = "memberId") String memberId) {
        service.deleteCrewPostByMemberId(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("크루 게시글 삭제 완료");
    }

    @DeleteMapping(value = "/crew/post/all/{crewNo}")
    public ResponseEntity<?> deletePostByCrewNo(@PathVariable(value = "crewNo") Long crewNo) {
        service.deleteCrewPostByCrewNo(crewNo);
        return ResponseEntity.status(HttpStatus.OK).body("크루 게시글 삭제 완료");
    }

    @PostMapping(value = "/crew/post/images/{imageUUID}")
    public ResponseEntity<String> insertCrewPostImage(@PathVariable("imageUUID") String uuid,
                                                      @RequestParam MultipartFile imgFile) {
        return ResponseEntity.status(HttpStatus.OK).body(fileUtil.saveCrewPostImg(uuid, imgFile));
    }

    @ResponseBody
    @GetMapping(value = "/crew/post/images/{imageUUID}/{imageFileName}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getCrewPostImage(@PathVariable("imageUUID") String uuid,
                                   @PathVariable("imageFileName") String imageFileName) throws IOException {
        String imgPath = System.getProperty("user.dir") + "/src/main/resources/static/crew_post_image/" + uuid + "/" + imageFileName;
        InputStream in = new FileInputStream(imgPath);
        byte[] imageByteArr = IOUtils.toByteArray(in);
        in.close();
        return imageByteArr;
    }

    @PostMapping(value = "/crew/post/{crewPostNo}/member")
    public ResponseEntity<?> tagMembers(@PathVariable(value = "crewPostNo") Long crewPostNo,
                                        @RequestParam String memberId,
                                        @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, service.getWriterId(crewPostNo))) {
            service.saveMembers(crewPostNo, memberId);
            return ResponseEntity.status(HttpStatus.OK).body("member added");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }

    @DeleteMapping(value = "/crew/post/{crewPostNo}/member")
    public ResponseEntity<?> untagMembers(@PathVariable(value = "crewPostNo") Long crewPostNo,
                                          @RequestParam String memberId,
                                          @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, memberId)) {
            service.deleteTaggedMember(new TaggedMemberId(crewPostNo, memberId));
            return ResponseEntity.status(HttpStatus.OK).body("member deleted");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }
}
