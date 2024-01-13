package com.example.demae.repository;

import com.example.demae.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
	List<Picture> findByMenuId(Long menuId);
}
