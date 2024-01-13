package com.example.demae.controller;

import com.example.demae.service.AwsS3Service;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/aws")
public class AwsController {
	private final AwsS3Service awsS3Service;

	@PostMapping("/{itemId}")
	public void postMember(
			@PathVariable("itemId") @Positive long itemId,
			@RequestParam(value = "file") List<MultipartFile> files) throws IOException {
		awsS3Service.uploadFiles(files, itemId);
	}
}
