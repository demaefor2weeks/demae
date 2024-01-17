package com.example.demae.service;

import com.example.demae.dto.cart.CartListResponseDto;
import com.example.demae.dto.cart.CartResponseDto;
import com.example.demae.dto.order.OrderAllResponseDto;
import com.example.demae.dto.order.OrderMenuRequestDto;
import com.example.demae.dto.order.OrderMenuResponseDto;
import com.example.demae.dto.order.OrderRequestDto;
import com.example.demae.dto.order.OrderResponseDto;
import com.example.demae.entity.Cart;
import com.example.demae.entity.Menu;
import com.example.demae.entity.Order;
import com.example.demae.entity.OrderList;
import com.example.demae.entity.Store;
import com.example.demae.entity.User;
import com.example.demae.enums.OrderState;
import com.example.demae.repository.CartRepository;
import com.example.demae.repository.OrderListRepository;
import com.example.demae.repository.OrderRepository;
import com.example.demae.repository.StoreRepository;
import com.example.demae.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final OrderListRepository orderListRepository;
	private final CartRepository cartRepository;
	@Transactional
	public Order createOrder(OrderRequestDto orderRequestDto, User user) {
		if (user.getPoint() < orderRequestDto.getTotalPrice()) {
			return null;
		}
		Store store = storeRepository.findById(orderRequestDto.getCartList().get(0).getStoreId()).orElseThrow();
		Order order = orderRepository.save(new Order(user, store));

		for (OrderMenuRequestDto orderMenuRequestDto : orderRequestDto.getCartList()) {
			OrderList orderList = new OrderList(order, orderMenuRequestDto);
			orderListRepository.save(orderList);
		}

		User findUser = userRepository.findById(user.getId()).orElseThrow();
		cartRepository.deleteByUser(findUser);

		findUser.setPoint(findUser.getPoint() - orderRequestDto.getTotalPrice());
		return order;
	}

	public OrderResponseDto getOrder(Long orderId, User user) {
		Order findOrder = orderRepository.findById(orderId).orElseThrow();
		if (user.getStore() != null && user.getStore().getId().equals(findOrder.getStore().getId()))  {
			return createOrderResponse(orderListRepository.findByOrderId(orderId));
		}
		throw new IllegalStateException("본인 가게 정보만 조회가 가능합니다.");
	}

	public OrderResponseDto createOrderResponse(List<OrderList> orderLists) {
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		for (OrderList orderList : orderLists) {
			OrderMenuResponseDto menuDto = new OrderMenuResponseDto(orderList);
			orderResponseDto.addItem(menuDto);
			orderResponseDto.addToTotalPrice(orderList.getPrice() * orderList.getQuantity());
		}
		return orderResponseDto;
	}

	public List<OrderAllResponseDto> getAllOrderInfo(User user) {

		List<OrderAllResponseDto> orderAllResponseDtoList = new ArrayList<>();
		List<Order> OrderList = orderRepository.findByStoreId(user.getStore().getId());
		for (Order order : OrderList) {
			OrderAllResponseDto orderAllResponseDto = new OrderAllResponseDto(order);
			orderAllResponseDtoList.add(orderAllResponseDto);
		}
		return orderAllResponseDtoList;
	}

	@Transactional
	public Order completeOrder(Long orderId, User user) {
		Order findOrder = orderRepository.findById(orderId).orElseThrow();
		if (user.getStore() != null && user.getStore().getId().equals(findOrder.getStore().getId()))  {
			Order order = orderRepository.findById(orderId).orElseThrow();
			order.setState(OrderState.CONFIRM);
			return order;
		}
		return null;
	}

	public List<OrderAllResponseDto> getAllOrderInfoUser(User user) {
		List<OrderAllResponseDto> orderResponseDto = new ArrayList<>();
		List<Order> orderList = orderRepository.findByUserId(user.getId());
		for(Order order:orderList){
			OrderAllResponseDto orderAllResponseDto = new OrderAllResponseDto(order);
			orderResponseDto.add(orderAllResponseDto);
		}
		return orderResponseDto;
	}
}
