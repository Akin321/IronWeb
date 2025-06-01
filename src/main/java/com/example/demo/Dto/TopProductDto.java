package com.example.demo.Dto;

import com.example.demo.model.ProductImages;
import com.example.demo.model.ProductModel;

public class TopProductDto {
	 private String name;
	    private String mainImageUrl;
	    private int sold;

	    // Constructor
	    public TopProductDto(ProductModel product, int sold) {
	        this.name = product.getName();
	        this.sold = sold;

	        // Find main image
	        this.mainImageUrl = product.getImages().stream()
	            .filter(ProductImages::getIs_main)
	            .map(ProductImages::getImage)
	            .findFirst()
	            .orElse(null); // Or some default image
	    }
	public TopProductDto() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainImageUrl() {
		return mainImageUrl;
	}
	public void setMainImageUrl(String mainImageUrl) {
		this.mainImageUrl = mainImageUrl;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	
    
}
