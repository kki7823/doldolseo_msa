package com.doldolseo.api.controller;

import com.doldolseo.api.dto.ReviewCommentRequest;
import com.doldolseo.api.dto.ReviewCommentResponse;
import com.doldolseo.api.dto.ReviewCommentsResponse;
import com.doldolseo.api.service.ReviewCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewCommentController {
    @Autowired
    ReviewCommentService service;

    @GetMapping(value = "/review/{reviewNo}/comment")
    public ResponseEntity<ReviewCommentsResponse> getReviewComment(@PathVariable("reviewNo") Long reviewNo) {
        return ResponseEntity.ok(service.getComments(reviewNo));
    }

    @PostMapping("/review/{reviewNo}/comment")
    public ResponseEntity<String> insertReviewComment(@PathVariable("reviewNo") Long reviewNo,
                                                      @RequestBody ReviewCommentRequest request) {
        service.insertComment(request);
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
                                                   @RequestBody ReviewCommentRequest request,
                                                   @RequestHeader String userId) {
        if (service.updateComment(commentNo, request, userId))
            return ResponseEntity.ok("등록 완료");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록 실패 : 권한 없음");
    }
}
