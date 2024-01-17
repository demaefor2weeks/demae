package com.example.demae.controller;

import com.example.demae.dto.order.OrderRequestDto;
import com.example.demae.dto.order.OrderStateDto;
import com.example.demae.entity.Order;
import com.example.demae.repository.UserRepository;
import com.example.demae.security.UserDetailsImpl;
import com.example.demae.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();



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
		return "orderAllInfoPage";
	}

	@PostMapping
	@ResponseBody
	public Long createOrder(@RequestBody OrderRequestDto orderRequestDto,
							  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.createOrder(orderRequestDto, userDetails.getUser());
		return userDetails.getUser().getId();
	}

	@GetMapping(value = "/connect/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect(String id,@PathVariable String userId) {
		// 유저가 SSE 연결을 요청할 때 사용
		SseEmitter emitter = new SseEmitter();


        // 유저별로 SSE 연결을 유지
		userEmitters.put(userId, emitter);

		emitter.onCompletion(() -> userEmitters.remove(id, emitter));
		emitter.onTimeout(() -> userEmitters.remove(id, emitter));

		return emitter;
	}


	@GetMapping(value = "/sse/{orderId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter completeOrder(@PathVariable Long orderId,
									@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Order order = orderService.completeOrder(orderId, userDetails.getUser());


		String userId = String.valueOf(order.getUser().getId()); // 주문을 한 유저의 고유 ID
		SseEmitter emitter = userEmitters.get(userId);

		try {
			if (emitter != null) {
				emitter.send("주문이 확인되었습니다.!!", MediaType.TEXT_EVENT_STREAM);
			}
		} catch (IOException e) {
			// 에러 처리
		}

		// 필요에 따라 emitters를 초기화하거나 관리할 수 있습니다.
		return emitter;
	}
}
