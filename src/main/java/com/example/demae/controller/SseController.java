package com.example.demae.controller;

import com.example.demae.dto.store.StoreResponseDto;
import com.example.demae.entity.Order;
import com.example.demae.entity.Store;
import com.example.demae.repository.StoreRepository;
import com.example.demae.security.UserDetailsImpl;
import com.example.demae.service.OrderService;
import com.example.demae.service.SseService;
import com.example.demae.service.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {
	private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000;
	private final OrderService orderService;
	//	private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();
	private final SseService sseService;
	private final StoreService storeService;
	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter connect(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		// 유저가 SSE 연결을 요청할 때 사용
//		String id = String.valueOf(userDetails.getUser().getId());
//		if(userEmitters.containsKey(String.valueOf(id))) {
//			SseEmitter sseEmitter = userEmitters.get(id);
//			userEmitters.remove(sseEmitter);
//		}
//
//		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//		userEmitters.put(id, emitter);
//		emitter.onCompletion(() -> userEmitters.remove(id, emitter));
//		emitter.onTimeout(() -> userEmitters.remove(id, emitter));
		SseEmitter emitter = sseService.createConnect(userDetails.getUser().getId());
		return emitter;
	}

	@GetMapping(value = "/{orderId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public void completeOrder(@PathVariable Long orderId,
										@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Order order = orderService.completeOrder(orderId, userDetails.getUser());
		String userId = String.valueOf(order.getUser().getId()); // 주문을 한 유저의 고유 ID
		String storeId = String.valueOf(order.getStore().getUser().getId());
		SseEmitter userEmitter = sseService.getUserEmitters(userId);
		SseEmitter storeEmitter = sseService.getUserEmitters(storeId);
		List<SseEmitter> emitters = Arrays.asList(userEmitter, storeEmitter);

		try {
			for (SseEmitter emitter : emitters) {
				if (emitter != null) {
					String jsonData = "{\"message\": \"주문이 확인되었습니다.!!\"}";
					emitter.send(SseEmitter.event()
							.data(jsonData, MediaType.TEXT_EVENT_STREAM));
					emitter.complete();
				}
			}
		} catch (IOException e) {
			// 에러 처리
		}
	}

	@GetMapping(value = "/user/{orderId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public void userRequestOrder(@PathVariable Long orderId,
							  @AuthenticationPrincipal UserDetailsImpl userDetails) {
		Order orderForUser = orderService.getOrderForUser(orderId, userDetails.getUser());
		Store storeForUser = storeService.findStoreForUser(orderForUser.getStore().getId());
		SseEmitter emitter = sseService.getUserEmitters(String.valueOf(storeForUser.getUser().getId()));
		String jsonData = "{\"message\": \"주문이 접수되었습니다.!!\"}";
		try {
			emitter.send(SseEmitter.event()
					.data(jsonData, MediaType.TEXT_EVENT_STREAM));
			emitter.complete();
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
