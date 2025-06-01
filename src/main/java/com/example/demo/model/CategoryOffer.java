package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;


@Entity

public class CategoryOffer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne()
	@JoinColumn(name="product_types_id")
	@JsonBackReference
	private ProductTypes productType;
	private String offerName;
	private BigDecimal categoryOffer;
	@Enumerated(EnumType.STRING)
	private OfferStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@PrePersist()
	public void onCreate() {
		this.createdAt=LocalDateTime.now();
	}
	
	@PreUpdate()
	public void onUpdate() {
		this.updatedAt=LocalDateTime.now();
	}
	public CategoryOffer(int id, ProductTypes productType, String offerName, BigDecimal categoryOffer,
			OfferStatus status) {
		super();
		this.id = id;
		this.productType = productType;
		this.offerName = offerName;
		this.categoryOffer = categoryOffer;
		this.status = status;
	}
	public CategoryOffer() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ProductTypes getProductType() {
		return productType;
	}
	public void setProductType(ProductTypes productType) {
		this.productType = productType;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public BigDecimal getCategoryOffer() {
		return categoryOffer;
	}
	public void setCategoryOffer(BigDecimal categoryOffer) {
		this.categoryOffer = categoryOffer;
	}
	public OfferStatus getStatus() {
		return status;
	}
	public void setStatus(OfferStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	
}
