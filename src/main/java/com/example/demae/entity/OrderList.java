package com.example.demae.entity;

import com.example.demae.dto.order.OrderMenuRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orderList")
public class OrderList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	private Order order;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false)
	private String menuName;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private Long storeId;



	public OrderList(Order order, OrderMenuRequestDto orderMenuRequestDto) {
		this.order = order;
		this.menuId = orderMenuRequestDto.getId();
		this.menuName = orderMenuRequestDto.getName();
		this.price = orderMenuRequestDto.getPrice();
		this.quantity = orderMenuRequestDto.getQuantity();
		this.storeId = orderMenuRequestDto.getStoreId();
	}
}
