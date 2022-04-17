package com.doldolseo.api.service;

import com.doldolseo.api.dto.ReviewRequest;
import com.doldolseo.api.dto.ReviewListResponse;
import com.doldolseo.api.entity.Review;
import com.doldolseo.api.repository.ReviewRepository;
import com.doldolseo.api.util.UploadReviewFileUtil;
import com.doldolseo.common.PagingUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository repository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UploadReviewFileUtil fileUtil;

    public ReviewListResponse getReviewList(Integer areaNo, Pageable pageable) {
        Page<ReviewRequest> reviewPage = getReviewPage(areaNo, pageable);
        ReviewListResponse response = populatePage(reviewPage);

        return response;
    }

    public ReviewListResponse populatePage(Page<ReviewRequest> reviewPage) {
        PagingUtil pagingUtil = new PagingUtil(5, reviewPage);

        ReviewListResponse reviewListResponse = new ReviewListResponse();
        reviewListResponse.setReviewList(reviewPage.getContent());
        reviewListResponse.setEndBlockPage(pagingUtil.startBlockPage);
        reviewListResponse.setEndBlockPage(pagingUtil.endBlockPage);
        reviewListResponse.setTotalPages(pagingUtil.totalPages);

        return reviewListResponse;
    }

    public Page<ReviewRequest> getReviewPage(Integer areaNo, Pageable pageable) {
        return (areaNo == null) ? getReviewPage(pageable) : getReviewPageByArea(areaNo, pageable);
    }

    public Page<ReviewRequest> getReviewPage(Pageable pageable) {
        Page<Review> reviewPage = repository.findAll(pageable);
        return entityPageToDtoPage(reviewPage);
    }

    public Page<ReviewRequest> getReviewPageByArea(Integer areaNo, Pageable pageable) {
        Page<Review> reviewPage = repository.findAllByAreaNo(areaNo, pageable);
        return entityPageToDtoPage(reviewPage);
    }

    public ReviewRequest getReview(Long reviewNo, String mode) {
        if (mode.equals("write"))
            return getReviewAndHit(reviewNo);
        else
            return getReview(reviewNo);
    }

    public ReviewRequest getReview(Long reviewNo) {
        Review review = repository.findByReviewNo(reviewNo);
        return entityToDto(review);
    }

    @Transactional
    public ReviewRequest getReviewAndHit(Long reviewNo) {
        Review review = repository.findByReviewNo(reviewNo);
        increaseHit(review);
        return entityToDto(review);
    }

    @Transactional
    public void increaseHit(Review review) {
        review.setHit(review.getHit() + 1);
    }

    public ReviewRequest insertReview(ReviewRequest dto) {
        getDTOfilledValues(dto);
        Review review = repository.save(dtoToEntity(dto));
        return entityToDto(review);
    }

    public void getDTOfilledValues(ReviewRequest dto) {
        dto.setHit(1);
        dto.setWDate(LocalDateTime.now());
    }

    @Transactional
    public boolean updateReview(Long reviewNo, ReviewRequest dto, String idTryToUpdate) {
        String writerId = getReview(reviewNo).getId();
        if (!writerId.equals(idTryToUpdate)) {
            System.out.println("[Update Fail] Has no Authority ");
            return false;
        }

        Review review = repository.findByReviewNo(reviewNo);
        review.setTitle(dto.getTitle());
        review.setContent(dto.getContent());
        review.setAreaNo(dto.getAreaNo());
        return true;
    }

    public boolean deleteReview(Long reviewNo, String idTryToDelete) {
        String writerId = getReview(reviewNo).getId();

        if (!writerId.equals(idTryToDelete)) {
            System.out.println("[DELETE Fail] Has no Authority ");
            return false;
        }

        fileUtil.deleteReviewImgs(getReview(reviewNo).getImageUUID());
        repository.deleteById(reviewNo);
        System.out.println(reviewNo + "번 게시글 삭제");
        return true;
    }

    public Review dtoToEntity(ReviewRequest dto) {
        return modelMapper.map(dto, Review.class);
    }

    public ReviewRequest entityToDto(Review review) {
        return modelMapper.map(review, ReviewRequest.class);
    }

    public Page<ReviewRequest> entityPageToDtoPage(Page<Review> reviewPage) {
        return modelMapper.map(reviewPage, new TypeToken<Page<ReviewRequest>>() {
        }.getType());
    }
}
