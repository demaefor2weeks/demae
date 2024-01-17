package com.example.demae.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name ="store_id",nullable = false)
    @JsonIgnore
    private Store store;

    public Menu(int price,String name,Store store) {
        this.name = name;
        this.price = price;
        this.store = store;
    }
    public void update(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
