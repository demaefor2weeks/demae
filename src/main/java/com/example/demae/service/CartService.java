package com.example.demae.service;

import com.example.demae.dto.cart.CartListResponseDto;
import com.example.demae.dto.cart.CartRequestDto;
import com.example.demae.dto.cart.CartResponseDto;
import com.example.demae.entity.Cart;
import com.example.demae.entity.Menu;
import com.example.demae.entity.Store;
import com.example.demae.entity.User;
import com.example.demae.repository.CartRepository;
import com.example.demae.repository.MenuRepository;
import com.example.demae.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
	private final MenuRepository menuRepository;
	private final CartRepository cartRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public String addToCart(CartRequestDto cartRequestDto, User user) {
		String is_ok = "False";
		Store store = storeRepository.findByMenusId(Long.valueOf(cartRequestDto.getMenuId()));
		Menu menu = menuRepository.findById(Long.valueOf(cartRequestDto.getMenuId())).
				orElseThrow(() -> new EntityNotFoundException("메뉴정보가 없습니다."));
		List<Cart> findCart = cartRepository.findByUserId(user.getId());
		if (findCart.isEmpty()) {
			cartRepository.save(new Cart(user, store, menu, cartRequestDto.getQuantity()));
			return "ok";
		} else {
			if (findCart.size() > 3) {
				return "size_error";
			}
			if (findCart.get(0).getStore().getId() != cartRequestDto.getStoreId()) {
				return "fail";
			} else {
				for (Cart Cart : findCart) {
					if (Cart.getMenu().getId() == cartRequestDto.getMenuId()) {
						Cart.setCount(Cart.getCount() + cartRequestDto.getQuantity());
						is_ok = "True";
					}
				}
				if (!is_ok.equals("True")){
					cartRepository.save(new Cart(user, store, menu, cartRequestDto.getQuantity()));
				}
				return "ok";
			}
		}
	}
	@Transactional
	public CartListResponseDto getCart(Long userId) {
		return createCartResponse(cartRepository.findByUserId(userId));
	}

	public CartListResponseDto createCartResponse(List<Cart> Carts) {
		CartListResponseDto cartResponseDto = new CartListResponseDto();
		for (Cart Cart : Carts) {
			Long menuId = Cart.getMenu().getId();
			int menuCount = Cart.getCount();
			Long userId = Cart.getUser().getId();

			Menu menu = menuRepository.findById(menuId)
					.orElseThrow(() -> new RuntimeException("Item not found"));
			CartResponseDto menuDto = new CartResponseDto(menu, Cart.getCount(),userId);
			cartResponseDto.addItem(menuDto);
			cartResponseDto.addToTotalPrice(menu.getPrice() * menuCount);
		}
		return cartResponseDto;
	}

	public void deleteCart(Long userId) {
		List<Cart> CartList = cartRepository.findByUserId(userId);
		cartRepository.deleteAll(CartList);
	}
}
