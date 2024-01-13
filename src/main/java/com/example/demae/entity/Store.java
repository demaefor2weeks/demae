package com.example.demae.entity;

import com.example.demae.dto.store.StoreRequestDto;
import com.example.demae.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String category;

    public Store(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.address = storeRequestDto.getAddress();
        this.category = storeRequestDto.getCategory();
    }
}
