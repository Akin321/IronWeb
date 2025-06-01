package com.example.demo.Dto;

public class CategorySalesDto {
	
	 private String name;
	    private String imageUrl;
	    private int sold;
		public CategorySalesDto(String name, String imageUrl, int sold) {
			super();
			this.name = name;
			this.imageUrl = imageUrl;
			this.sold = sold;
		}
		
		public CategorySalesDto() {
			
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public int getSold() {
			return sold;
		}
		public void setSold(int sold) {
			this.sold = sold;
		}
	    
	    
}
