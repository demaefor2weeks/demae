package com.example.demae.service;

import com.example.demae.entity.Order;
import com.example.demae.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseService {
	private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000;
	private final Map<String, SseEmitter> userEmitters = new ConcurrentHashMap<>();

	public SseEmitter createConnect(Long userId) {
		String id = String.valueOf(userId);
		if(userEmitters.containsKey(String.valueOf(id))) {
			SseEmitter sseEmitter = userEmitters.get(id);
			userEmitters.remove(sseEmitter);
		}

		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		userEmitters.put(id, emitter);
		emitter.onCompletion(() -> userEmitters.remove(id, emitter));
		emitter.onTimeout(() -> userEmitters.remove(id, emitter));

		return emitter;
	}

	public SseEmitter getUserEmitters(String userId) {
		return userEmitters.get(String.valueOf(userId));
	}

	public void deleteEmitters(SseEmitter emitter) {
		userEmitters.remove(emitter);
	}

	public List<SseEmitter> findUserAndStore(Order order){
		String userId = String.valueOf(order.getUser().getId()); // 주문을 한 유저의 고유 ID
		String storeId = String.valueOf(order.getStore().getUser().getId());
		SseEmitter userEmitter = getUserEmitters(userId);
		SseEmitter storeEmitter = getUserEmitters(storeId);
		return Arrays.asList(userEmitter, storeEmitter);
	}
}
