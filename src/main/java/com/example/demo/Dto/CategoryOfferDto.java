package com.example.demo.Dto;



import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryOfferDto {
	private int id;
	 @NotNull(message = "Product type ID cannot be null")
	    private Integer productTypeId;

	    @NotBlank(message = "Offer name is required")
	    private String offerName;

	    @NotNull(message = "Discount percentage is required")
	    @Min(value = 1, message = "Discount must be at least 1%")
	    @Max(value = 99, message = "Discount must be less than 100%")
	    private BigDecimal categoryOffer;
	    private String productTypeName;

		public CategoryOfferDto(int id, @NotNull(message = "Product type ID cannot be null") Integer productTypeId,
				@NotBlank(message = "Offer name is required") String offerName,
				@NotNull(message = "Discount percentage is required") @Min(value = 1, message = "Discount must be at least 1%") @Max(value = 99, message = "Discount must be less than 100%") BigDecimal categoryOffer,
				String productTypeName) {
			super();
			this.id = id;
			this.productTypeId = productTypeId;
			this.offerName = offerName;
			this.categoryOffer = categoryOffer;
			this.productTypeName = productTypeName;
		}

		public CategoryOfferDto() {
		
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	

		public Integer getProductTypeId() {
			return productTypeId;
		}

		public void setProductTypeId(Integer productTypeId) {
			this.productTypeId = productTypeId;
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

		public String getProductTypeName() {
			return productTypeName;
		}

		public void setProductTypeName(String productTypeName) {
			this.productTypeName = productTypeName;
		}
	    
	    
}
