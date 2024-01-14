package com.example.demae.controller;

import com.example.demae.dto.store.StoreRequestDto;
import com.example.demae.dto.store.StoreResponseDto;
import com.example.demae.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/stores")
@AllArgsConstructor
public class StoreController {
	private final StoreService storeService;

	@GetMapping("home")
	public String home(){return "store";}

	@PostMapping
	public String createStore(@RequestBody StoreRequestDto storeRequestDto) {
		storeService.createStores(storeRequestDto);
		return "showAllStorePage";
	}

	@GetMapping("/category")
	public String findByCategory(@RequestParam String category, Model model){
		model.addAttribute("storeList", storeService.findByCategory(category));
		return "showStorePage";
	}

	@GetMapping("/{storeId}")
	public String findStore(@PathVariable Long storeId, Model model){
		model.addAttribute("storeList", storeService.findStore(storeId));
		return "showStorePage";
	}

	@GetMapping
	public String findAllStore(@RequestParam(defaultValue = "0") int page,
							   @RequestParam(defaultValue = "10") int size,
							   Model model){
		model.addAttribute("storeList", storeService.findAll(page, size));
		return "showAllStorePage";
	}

	@PatchMapping("/{storeId}")
	@ResponseBody
	public ResponseEntity<String> modifyStore(@PathVariable Long storeId, @RequestBody StoreRequestDto storeRequestDto){
		try {
			storeService.modifyStore(storeId, storeRequestDto);
			return ResponseEntity.ok("ok");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.badRequest().body("fail");
		}
	}

	@DeleteMapping("/{storeId}")
	@ResponseBody
	public ResponseEntity<String> deleteStore(@PathVariable Long storeId){
		try {
			storeService.deleteStore(storeId);
			return ResponseEntity.ok("ok");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.badRequest().body("fail");
		}
	}
}
