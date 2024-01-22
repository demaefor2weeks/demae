package com.example.demae.service;

import com.example.demae.dto.menu.MenuResponseDto;
import com.example.demae.dto.review.ReviewRequestDto;
import com.example.demae.dto.review.ReviewResponseDto;
import com.example.demae.entity.Menu;
import com.example.demae.entity.Order;
import com.example.demae.entity.Review;
import com.example.demae.entity.Store;
import com.example.demae.repository.OrderRepository;
import com.example.demae.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public void createReview(Long orderId, ReviewRequestDto reviewRequestDto, Long id) {
        Order order = orderRepository.findByUserIdAndId(id, orderId);
        List<Review> review1 = reviewRepository.findByOrderId(orderId);
        for(Review review2:review1){
            if(review2.getOrder().getId().equals(orderId)){
                throw new IllegalArgumentException("리뷰가 존재합니다.");
            }
        }
        if (order == null) {
            throw new IllegalArgumentException("주문이 존재하지 않습니다.");
        }
        Review review = new Review(reviewRequestDto, order);
        reviewRepository.save(review);
    }

    public List<Review> getReview(Long orderId) {
        List<Review> reviewCheck = reviewRepository.findByOrderId(orderId);
        List<Review> newList = new ArrayList<>(reviewCheck);
        return newList;
    }

    public ReviewResponseDto singleMenu(Long orderId, Long reviewId) {
        Review checkReview = reviewRepository.findById(reviewId).orElseThrow(()->new IllegalArgumentException("리뷰가 없습니다."));
        return new ReviewResponseDto(checkReview);
    }

    @Transactional
    public ReviewResponseDto patchReview(Long orderId, Long reviewId,ReviewRequestDto reviewRequestDto,String email) {
        Order findOder = orderRepository.findById(orderId).orElseThrow(()-> new IllegalArgumentException("주문이 없습니다."));
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new IllegalArgumentException("리뷰가 없습니다."));
        if(!findOder.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        review.update(reviewRequestDto.getPoint(),reviewRequestDto.getContent());
        return new ReviewResponseDto(review);
    }
    @Transactional
    public void deleteReview(Long orderId, Long reviewId, String email) {
        Order FindOrder = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("주문이 없습니다"));
        Review findReview = reviewRepository.findById(reviewId).orElseThrow(()->new IllegalArgumentException("리뷰가 없습니다."));
        if(!FindOrder.getUser().getEmail().equals(email)){
            throw new IllegalArgumentException("본인의 가게가 아닙니다.");
        }
        reviewRepository.delete(findReview);
    }
}
