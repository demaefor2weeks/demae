package com.example.demae.repository;

import com.example.demae.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByName(String name);
	Optional<Store> findByCategory(String category);
}
