package com.doldolseo.api.repository;

import com.doldolseo.api.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAll(Pageable pageable);

    Page<Review> findAllByAreaNo(Integer areaNo, Pageable pageable);

    Review findByReviewNo(Long reviewNo);
}
