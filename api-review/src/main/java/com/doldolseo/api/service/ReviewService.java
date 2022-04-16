package com.doldolseo.api.service;

import com.doldolseo.api.dto.ReviewDTO;
import com.doldolseo.api.entity.Review;
import com.doldolseo.api.repository.ReviewRepository;
import com.doldolseo.api.util.UploadReviewFileUtil;
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

    public Page<ReviewDTO> getReviewPage(Integer areaNo, Pageable pageable) {
        return (areaNo == null) ? getReviewPage(pageable) : getReviewPageByArea(areaNo, pageable);
    }

    public Page<ReviewDTO> getReviewPage(Pageable pageable) {
        Page<Review> reviewPage = repository.findAll(pageable);
        return entityPageToDtoPage(reviewPage);
    }

    public Page<ReviewDTO> getReviewPageByArea(Integer areaNo, Pageable pageable) {
        Page<Review> reviewPage = repository.findAllByAreaNo(areaNo, pageable);
        return entityPageToDtoPage(reviewPage);
    }

    public ReviewDTO getReview(Long reviewNo) {
        Review review = repository.findByReviewNo(reviewNo);
        return entityToDto(review);
    }

    @Transactional
    public ReviewDTO getReviewAndHit(Long reviewNo) {
        Review review = repository.findByReviewNo(reviewNo);
        increaseHit(review);
        return entityToDto(review);
    }

    @Transactional
    public void increaseHit(Review review) {
        review.setHit(review.getHit() + 1);
    }

    public ReviewDTO insertReview(ReviewDTO dto) {
        getDTOfilledValues(dto);
        Review review = repository.save(dtoToEntity(dto));
        return entityToDto(review);
    }

    public void getDTOfilledValues(ReviewDTO dto) {
        dto.setHit(1);
        dto.setWDate(LocalDateTime.now());
    }

    @Transactional
    public boolean updateReview(Long reviewNo, ReviewDTO dto, String idTryToUpdate) {
        String writerId = getReview(reviewNo).getId();
        if(!writerId.equals(idTryToUpdate)) {
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

        if(!writerId.equals(idTryToDelete)) {
            System.out.println("[DELETE Fail] Has no Authority ");
            return false;
        }

        fileUtil.deleteReviewImgs(getReview(reviewNo).getImageUUID());
        repository.deleteById(reviewNo);
        System.out.println(reviewNo + "번 게시글 삭제");
        return true;
    }

    public Review dtoToEntity(ReviewDTO dto) {
        return modelMapper.map(dto, Review.class);
    }

    public ReviewDTO entityToDto(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

    public Page<ReviewDTO> entityPageToDtoPage(Page<Review> reviewPage) {
        return modelMapper.map(reviewPage, new TypeToken<Page<ReviewDTO>>() {
        }.getType());
    }
}
