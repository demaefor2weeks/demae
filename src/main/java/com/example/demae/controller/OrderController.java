package com.example.demae.controller;

import com.example.demae.dto.order.OrderRequestDto;
import com.example.demae.entity.Order;
import com.example.demae.jwt.JwtUtil;
import com.example.demae.security.UserDetailsImpl;
import com.example.demae.service.OrderService;
import com.example.demae.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;
	private final SseService sseService;

	@GetMapping("/{orderId}")
	public String getOrder(@PathVariable Long orderId, Model model,
						   @AuthenticationPrincipal UserDetailsImpl userDetails) {
		model.addAttribute("order", orderService.getOrder(orderId, userDetails.getUser()));
		return "orderInfoPage";
	}

	@GetMapping
	public String getAllOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
							  Model model) {
		if(userDetails.getUser().getRole().name().equals("USER")) {
			model.addAttribute("orderAllUser",orderService.getAllOrderInfoUser(userDetails.getUser()));
			return "orderAllInfoPageUser";
		}
		model.addAttribute("orderAll", orderService.getAllOrderInfo(userDetails.getUser()));
//    	model.addAttribute("token", token);
		return "orderAllInfoPage";
	}

//	@PostMapping
//	@ResponseBody
//	public Long createOrder(@RequestBody OrderRequestDto orderRequestDto,
//							  @AuthenticationPrincipal UserDetailsImpl userDetails) {
//		Order order = orderService.createOrder(orderRequestDto, userDetails.getUser());
//
//		return userDetails.getUser().getId();
//	}

	@PostMapping
	@ResponseBody
	public Long createOrder(@RequestBody OrderRequestDto orderRequestDto,
							@AuthenticationPrincipal UserDetailsImpl userDetails) {
		Order order = orderService.createOrder(orderRequestDto, userDetails.getUser());

		return order.getId();
	}
}
