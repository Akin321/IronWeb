package com.example.demo.Dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BannerDto {
		private int id;
	    private String title;
	    @Size(max=100,message="max 50 characters are allowed")
	    private String description;
	    @NotNull(message="image cannot be null")
	    private MultipartFile imageUrl;
	    private boolean enabled;
	    @Min(value=1,message="minimum value should be 1")
	    @Max(value=5,message="maximum value should be 5")
	    private Integer displayOrder;
	    private String image;
		
		
		public BannerDto(int id, String title,
				@Size(max = 100, message = "max 50 characters are allowed") String description,
				@NotNull(message = "image cannot be null") MultipartFile imageUrl, boolean enabled,
				 @Min(value = 1, message = "minimum value should be 1") @Max(value = 5, message = "maximum value should be 5") Integer displayOrder,
				String image) {
			super();
			this.id = id;
			this.title = title;
			this.description = description;
			this.imageUrl = imageUrl;
			this.enabled = enabled;
			this.displayOrder = displayOrder;
			this.image = image;
		}

		public BannerDto() {
		
		}
		

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
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
		public MultipartFile getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(MultipartFile imageUrl) {
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
