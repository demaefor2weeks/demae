package com.example.demae.service;

import com.example.demae.dto.store.StoreRequestDto;
import com.example.demae.dto.store.StoreResponseDto;
import com.example.demae.entity.Store;
import com.example.demae.entity.User;
import com.example.demae.repository.StoreRepository;
import com.example.demae.repository.UserRepository;
import com.example.demae.security.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	@Transactional
	public StoreResponseDto createStores(StoreRequestDto storeRequestDto, User user) {
		if (storeRepository.findByUserId(user.getId()).isPresent()) {
			throw new IllegalArgumentException("이미 가입된 계정입니다.");
		}
		Store saveStore = storeRepository.save(new Store(storeRequestDto, user));
		User user1 = userRepository.findById(user.getId()).orElseThrow();
		user1.setStore(saveStore);

		return new StoreResponseDto().success(saveStore);
	}

	public StoreResponseDto findStore(Long storeId) {
		return new StoreResponseDto().success(storeRepository.findById(storeId)
				.orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId)));
	}

	public List<StoreResponseDto> findByCategory(String category) {
		return storeRepository.findByCategory(category).stream()
				.map(store -> new StoreResponseDto().success(store))
				.toList();
	}

	@Transactional
	public void modifyStore(Long storeId, StoreRequestDto storeRequestDto, String userName) {
		Store store = storeRepository.findById(storeId)
				.orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));
		if (!store.getUser().getEmail().equals(userName)) {
			throw new IllegalArgumentException("본인 정보만 수정이 가능합니다.");
		}
		store.setName(storeRequestDto.getName());
		store.setAddress(storeRequestDto.getAddress());
		store.setCategory(storeRequestDto.getCategory());
	}

	@Transactional
	public void deleteStore(Long storeId, String userName) {
		Store store = storeRepository.findById(storeId)
				.orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));
		if (!store.getUser().getEmail().equals(userName)) {
			throw new IllegalArgumentException("본인 정보만 수정이 가능합니다.");
		}
		storeRepository.delete(store);
	}

	public List<StoreResponseDto> findAll(int page, int size) {
		return storeRepository.findAll(PageRequest.of(page, size)).stream()
				.map(store -> new StoreResponseDto().success(store))
				.toList();
	}

	public Store findStoreForUser(Long storeId) {
		return storeRepository.findById(storeId).orElseThrow();
	}
}
