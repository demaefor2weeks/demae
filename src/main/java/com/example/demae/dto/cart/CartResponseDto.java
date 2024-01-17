package com.example.demae.dto.cart;

import com.example.demae.entity.Menu;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDto {
	private Long id;
	private String name;
	private int price;
	private int quantity;
	private Long storeId;
	private Long userId;

	public CartResponseDto(Menu menu, int quantity,Long userId){
		this.id = menu.getId();
		this.name =menu.getName();
		this.price = menu.getPrice();
		this.storeId = menu.getStore().getId();
		this.quantity = quantity;
		this.userId =userId;
	}
}
