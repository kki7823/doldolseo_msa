package com.doldolseo.api.review_comment_rest;

import com.doldolseo.api.dto.ReviewCommentDTO;
import com.doldolseo.api.dto.ReviewCommentsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewCommentController {
    @Autowired
    ReviewCommentService service;

    @GetMapping(value = "/review/{reviewNo}/comment")
    public ResponseEntity<ReviewCommentsDTO> getReviewComment(@PathVariable("reviewNo") Long reviewNo) {
        return ResponseEntity.ok(service.getComments(reviewNo));
    }

    @PostMapping("/review/{reviewNo}/comment")
    public ResponseEntity<String> insertReviewComment(@PathVariable("reviewNo") Long reviewNo,
                                                      @RequestBody ReviewCommentDTO dto) {
        service.insertComment(dto);
        return ResponseEntity.ok("댓글 삽입 완료");
    }

    @DeleteMapping("/review/{reviewNo}/comment/{commentNo}")
    public ResponseEntity<String> deleteReviewComment(@PathVariable("reviewNo") Long reviewNo,
                                                      @PathVariable("commentNo") Long commentNo,
                                                      @RequestHeader String userId) {

        if (service.deleteComment(commentNo, userId))
            return ResponseEntity.ok("삭제 완료");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("삭제 실패 : 권한 없음");
    }

    @PutMapping("/review/{reviewNo}/comment/{commentNo}")
    public ResponseEntity<String> putReviewComment(@PathVariable("reviewNo") Long reviewNo,
                                                   @PathVariable("commentNo") Long commentNo,
                                                   @RequestBody ReviewCommentDTO dto,
                                                   @RequestHeader String userId) {
        if (service.updateComment(commentNo, dto, userId))
            return ResponseEntity.ok("등록 완료");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록 실패 : 권한 없음");
    }
}
