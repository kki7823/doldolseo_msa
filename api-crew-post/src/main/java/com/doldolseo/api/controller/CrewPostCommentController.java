package com.doldolseo.api.controller;

import com.doldolseo.api.dto.CrewPostCommentsDTO;
import com.doldolseo.api.dto.CrewPostCommentDTO;
import com.doldolseo.api.service.CrewPostCommnetService;
import com.doldolseo.api.utils.AuthorityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CrewPostCommentController {
    @Autowired
    CrewPostCommnetService service;
    @Autowired
    AuthorityUtil authorityUtil;

    @GetMapping(value = "/crew/post/{crewPostNo}/comment")
    public ResponseEntity<CrewPostCommentsDTO> getCrewPostComment(@PathVariable("crewPostNo") Long crewPostNo) {
        return ResponseEntity.ok(service.getComments(crewPostNo));
    }

    @PostMapping("/crew/post/{crewPostNo}/comment")
    public ResponseEntity<String> insertCrewPostComment(@PathVariable("crewPostNo") Long crewPostNo,
                                                        @RequestBody CrewPostCommentDTO dto) {
        service.insertComment(dto);
        return ResponseEntity.ok("댓글 삽입 완료");
    }

    @PutMapping("/crew/post/{crewPostNo}/comment/{commentNo}")
    public ResponseEntity<?> putCrewPostComment(@PathVariable("crewPostNo") Long crewPostNo,
                                                @PathVariable("commentNo") Long commentNo,
                                                @RequestBody CrewPostCommentDTO dto,
                                                @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, service.getCommentWriter(commentNo))) {
            service.updateComment(commentNo, dto);
            return ResponseEntity.ok(commentNo + "번 댓글 수정");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }

    @DeleteMapping("/crew/post/{crewPostNo}/comment/{commentNo}")
    public ResponseEntity<?> deleteCrewPostComment(@PathVariable("crewPostNo") Long crewPostNo,
                                                   @PathVariable("commentNo") Long commentNo,
                                                   @RequestHeader String userId) {
        if (authorityUtil.isYou(userId, service.getCommentWriter(commentNo))) {
            service.deleteComment(commentNo);
            return ResponseEntity.ok(commentNo + "번 댓글 삭제");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Fail");
        }
    }
}
