package com.doldolseo.api.controller;

import com.doldolseo.api.dto.ReviewRequest;
import com.doldolseo.api.dto.ReviewListResponse;
import com.doldolseo.api.service.ReviewService;
import com.doldolseo.api.util.UploadReviewFileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
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

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    UploadReviewFileUtil fileUtil;

    @GetMapping("/review")
    public ResponseEntity<ReviewListResponse> getReviewList(@RequestParam(name = "areaNo", required = false) Integer areaNo,
                                                            @PageableDefault(size = 30, sort = "wDate", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewList(areaNo, pageable));
    }

    @GetMapping("/review/{reviewNo}")
    public ResponseEntity<ReviewRequest> getReview(@PathVariable Long reviewNo,
                                                   @RequestParam String mode) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReview(reviewNo, mode));
    }

    @PostMapping(value = "/review")
    @ResponseBody
    public ResponseEntity<Long> insertReview(ReviewRequest dto) {
        reviewService.insertReview(dto);
        return ResponseEntity.status(HttpStatus.OK).body(dto.getReviewNo());
    }

    @PostMapping(value = "/review/images/{imageUUID}")
    public ResponseEntity<String> insertReviewImage(@PathVariable("imageUUID") String uuid,
                                                    @RequestParam MultipartFile imgFile) throws IOException, FileSizeLimitExceededException {
        return ResponseEntity.status(HttpStatus.OK).body(fileUtil.saveReviewImg(uuid, imgFile));
    }

    @PutMapping(value = "/review/{reviewNo}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewNo") Long reviewNo,
                                               ReviewRequest dto,
                                               @RequestHeader String userId) {
        if (reviewService.updateReview(reviewNo, dto, userId))
            return ResponseEntity.status(HttpStatus.OK).body(reviewNo + "번 게시글 수정 완료");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(reviewNo + "번 게시글 수정 실패");
    }

    @DeleteMapping(value = "/review/{reviewNo}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewNo") Long reviewNo,
                                               @RequestHeader String userId) {
        if (reviewService.deleteReview(reviewNo, userId))
            return ResponseEntity.status(HttpStatus.OK).body(reviewNo + "번 게시글 삭제");
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(reviewNo + "번 게시글 삭제 실패");
    }

    @ResponseBody
    @GetMapping(value = "/review/images/{imageUUID}/{imageFileName}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] getReviewImage(@PathVariable("imageUUID") String uuid,
                                 @PathVariable("imageFileName") String imageFileName) throws IOException {
        String imgPath = System.getProperty("user.dir") + "/src/main/resources/static/review_image/" + uuid + "/" + imageFileName;
        InputStream in = new FileInputStream(imgPath);
        byte[] imageByteArr = IOUtils.toByteArray(in);
        in.close();
        return imageByteArr;
    }

    @PostMapping(value = "/review/course/{imageUUID}")
    public ResponseEntity<String> insertReviewCourse(@PathVariable("imageUUID") String imageUUID,
                                                     @RequestParam MultipartFile courseImgFile) throws IOException, FileSizeLimitExceededException {
        return ResponseEntity.status(HttpStatus.OK).body(fileUtil.saveCourseImg(imageUUID, courseImgFile));
    }
}
