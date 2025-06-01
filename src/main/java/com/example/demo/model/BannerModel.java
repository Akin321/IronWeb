package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class BannerModel {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private String title;
	    private String description;
	    private String imageUrl;
	    private boolean enabled;
	    @Column(unique=true)
	    private Integer displayOrder;
	    private LocalDateTime createdAt;
	    public void onCreate() {
	    	this.createdAt=LocalDateTime.now();
	    }
		public BannerModel(int id, String title, String description, String imageUrl, boolean enabled,
				Integer displayOrder) {
			super();
			this.id = id;
			this.title = title;
			this.description = description;
			this.imageUrl = imageUrl;
			this.enabled = enabled;
			this.displayOrder = displayOrder;
		}
		
		public BannerModel() {
		
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public Integer getDisplayOrder() {
			return displayOrder;
		}
		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}
	    
	    
}
