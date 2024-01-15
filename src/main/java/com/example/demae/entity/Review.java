package com.example.demae.entity;

import com.example.demae.dto.review.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long point;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Review(ReviewRequestDto reviewRequestDto, Order order) {
        this.point = reviewRequestDto.getPoint();
        this.content = reviewRequestDto.getContent();
        this.order = order;
    }
}
