package com.doldolseo.api.service;

import com.doldolseo.api.dto.ReviewCommentRequest;
import com.doldolseo.api.dto.ReviewCommentResponse;
import com.doldolseo.api.dto.ReviewCommentsResponse;
import com.doldolseo.api.entity.Review;
import com.doldolseo.api.entity.ReviewComment;
import com.doldolseo.api.repository.ReviewCommentRepository;
import com.doldolseo.api.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewCommentService {
    @Autowired
    ReviewCommentRepository reviewCommentRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ModelMapper modelMapper;

    public void insertComment(ReviewCommentRequest request) {
        request.setWDate(LocalDateTime.now());
        setReview(request);
        ReviewComment comment = reviewCommentRepository.save(dtoToEntity(request));
    }

    public void setReview(ReviewCommentRequest dto) {
        Review review = reviewRepository.findByReviewNo(dto.getReview().getReviewNo());
        dto.setReview(review);
    }

    public ReviewCommentsResponse getComments(Long reviewNo) {
        ReviewCommentsResponse commentsDTO = new ReviewCommentsResponse();

        List<ReviewComment> commentEntity = reviewCommentRepository.findAllByReview_ReviewNo(reviewNo);
        commentsDTO.setComments(entityListToDtoList(commentEntity));

        Long numOfCommnets = reviewCommentRepository.countReviewCommentsByReview_ReviewNo(reviewNo);
        commentsDTO.setNumOfComments(numOfCommnets);

        return commentsDTO;
    }

    @Transactional
    public boolean updateComment(Long commentNo, ReviewCommentRequest request, String idTryToUpdate) {
        String writerId = reviewCommentRepository.findByCommentNo(commentNo).getId();

        if (!writerId.equals(idTryToUpdate)) {
            System.out.println("[Update Fail] Has no Authority ");
            return false;
        }

        ReviewComment comment = reviewCommentRepository.findByCommentNo(commentNo);
        comment.setWDate(LocalDateTime.now());
        comment.setContent(request.getContent());
        return true;
    }

    public boolean deleteComment(Long commentNo, String idTryToDelete) {
        String writerId = reviewCommentRepository.findByCommentNo(commentNo).getId();

        if (!writerId.equals(idTryToDelete)) {
            System.out.println("[Delete Fail] Has no Authority ");
            return false;
        }

        reviewCommentRepository.deleteById(commentNo);
        return true;
    }

    public ReviewComment dtoToEntity(ReviewCommentRequest dto) {
        return modelMapper.map(dto, ReviewComment.class);
    }

    public ReviewCommentResponse entityToDto(ReviewComment comment) {
        return modelMapper.map(comment, ReviewCommentResponse.class);
    }

    public List<ReviewCommentResponse> entityListToDtoList(List<ReviewComment> commentList) {
        return modelMapper.map(commentList, new TypeToken<List<ReviewCommentResponse>>() {
        }.getType());
    }

}
