package com.example.demae.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name ="menu_id")
	@JsonIgnore
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="user_id")
	@JsonIgnore
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="store_id")
	@JsonIgnore
	private Store store;

	@Column(nullable = false)
	private int count;


	public Cart(User user, Store store, Menu menu, int count) {
		this.menu = menu;
		this.user = user;
		this.store = store;
		this.count = count;
	}

}
