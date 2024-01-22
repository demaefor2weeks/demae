package com.example.demae.controller;

import com.example.demae.dto.review.ReviewRequestDto;
import com.example.demae.dto.review.ReviewResponseDto;
import com.example.demae.entity.Review;
import com.example.demae.security.UserDetailsImpl;
import com.example.demae.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/{orderId}/createreview")
    public String showCreateReviewPage(Model model,@PathVariable Long orderId){
        model.addAttribute("orderId",orderId);
        return "createReview";
    }
    @PostMapping("/{orderId}/review")
    public String createReview(@PathVariable Long orderId,
                               @RequestBody ReviewRequestDto reviewRequestDto,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long id = userDetails.getUser().getId();
        reviewService.createReview(orderId,reviewRequestDto,id);
        return "showReview";
    }

    @GetMapping("/{orderId}/multiReview")
    public String multiReview(@PathVariable Long orderId,
                            Model model){
        List<Review> review = reviewService.getReview(orderId);
        model.addAttribute("reviewList", review);
        return "showReview";
    }

    @GetMapping("/{orderId}/singleReview/{reviewId}")
    public String singleReview(@PathVariable Long orderId,
                               @PathVariable Long reviewId,
                               Model model){
        ReviewResponseDto selectReview = reviewService.singleMenu(orderId,reviewId);
        model.addAttribute("reviewOne",selectReview);
        return "showSingleReview";
    }
    @PatchMapping("/{orderId}/patchReview/{reviewId}")
    public String patchReview(@PathVariable Long orderId,
                              @PathVariable Long reviewId,
                              @RequestBody ReviewRequestDto reviewRequestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              Model model){
        String email = userDetails.getUser().getEmail();
        ReviewResponseDto reviewResponseDto = reviewService.patchReview(orderId, reviewId, reviewRequestDto, email);
        model.addAttribute("reviewOne",reviewResponseDto);
        return "showSingleReview";
    }
    @DeleteMapping("/{orderId}/deleteReview/{reviewId}")
    @ResponseBody
    public void deleteReview(@PathVariable Long orderId,
                               @PathVariable Long reviewId,
                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                               Model model){
        String email = userDetails.getUser().getEmail();
        reviewService.deleteReview(orderId,reviewId,email);
    }
}
