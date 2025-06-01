package com.example.demo.model;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;

@Entity
public class ProductTypes {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(columnDefinition = "TEXT")
	private String description;
	private String image;
	private String name;
	private boolean isActive =true;
	private LocalDateTime createdAt;
	@OneToMany(mappedBy = "productType", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<CategoryOffer> categoryOffers;

	
	public ProductTypes() {

	}

	
	public ProductTypes(int id, Gender gender, String description, String image, String name, boolean isActive,
			LocalDateTime createdAt, List<CategoryOffer> categoryOffer) {
		super();
		this.id = id;
		this.gender = gender;
		this.description = description;
		this.image = image;
		this.name = name;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.categoryOffers = categoryOffer;
	}


	@PrePersist
	public void onCreate() {
		this.createdAt=LocalDateTime.now();
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime created_at) {
		this.createdAt = created_at;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIs_active() {
		return isActive;
	}
	public void setIs_active(boolean is_active) {
		this.isActive = is_active;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public List<CategoryOffer> getCategoryOffers() {
		return categoryOffers;
	}


	public void setCategoryOffers(List<CategoryOffer> categoryOffers) {
		this.categoryOffers = categoryOffers;
	}





	

	
	
}
