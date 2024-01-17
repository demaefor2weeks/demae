package com.example.demae.entity;

import com.example.demae.enums.OrderState;
import com.example.demae.enums.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnore
    private Store store;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderState state  = OrderState.READY;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    public Order(User user, Store store) {
        this.user = user;
        this.store = store;
    }
}
