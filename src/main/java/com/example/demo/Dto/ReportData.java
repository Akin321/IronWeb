package com.example.demo.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportData {
	 private LocalDate startDate;
	    private LocalDate endDate;
	    private String generatedDate;

	    private long totalOrders;
	    private BigDecimal totalSalesAmount;
	    private BigDecimal totalDiscount;
	    private BigDecimal couponDiscount;
	    private String salesChartBase64;

	    private List<TopProductDto> topProducts;
	    private List<CategorySalesDto> categories;
		public ReportData(LocalDate startDate, LocalDate endDate, String generatedDate, long totalOrders,
				BigDecimal totalSalesAmount, BigDecimal totalDiscount, BigDecimal couponDiscount,
				String salesChartBase64, List<TopProductDto> topProducts, List<CategorySalesDto> categories) {
			super();
			this.startDate = startDate;
			this.endDate = endDate;
			this.generatedDate = generatedDate;
			this.totalOrders = totalOrders;
			this.totalSalesAmount = totalSalesAmount;
			this.totalDiscount = totalDiscount;
			this.couponDiscount = couponDiscount;
			this.salesChartBase64 = salesChartBase64;
			this.topProducts = topProducts;
			this.categories = categories;
		}
		public ReportData() {
		}
		public LocalDate getStartDate() {
			return startDate;
		}
		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}
		public LocalDate getEndDate() {
			return endDate;
		}
		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}
		public String getGeneratedDate() {
			return generatedDate;
		}
		public void setGeneratedDate(String generatedDate) {
			this.generatedDate = generatedDate;
		}
		public long getTotalOrders() {
			return totalOrders;
		}
		public void setTotalOrders(long totalOrders) {
			this.totalOrders = totalOrders;
		}
		public BigDecimal getTotalSalesAmount() {
			return totalSalesAmount;
		}
		public void setTotalSalesAmount(BigDecimal totalSalesAmount) {
			this.totalSalesAmount = totalSalesAmount;
		}
		public BigDecimal getTotalDiscount() {
			return totalDiscount;
		}
		public void setTotalDiscount(BigDecimal totalDiscount) {
			this.totalDiscount = totalDiscount;
		}
		public BigDecimal getCouponDiscount() {
			return couponDiscount;
		}
		public void setCouponDiscount(BigDecimal couponDiscount) {
			this.couponDiscount = couponDiscount;
		}
		public String getSalesChartBase64() {
			return salesChartBase64;
		}
		public void setSalesChartBase64(String salesChartBase64) {
			this.salesChartBase64 = salesChartBase64;
		}
		public List<TopProductDto> getTopProducts() {
			return topProducts;
		}
		public void setTopProducts(List<TopProductDto> topProducts) {
			this.topProducts = topProducts;
		}
		public List<CategorySalesDto> getCategories() {
			return categories;
		}
		public void setCategories(List<CategorySalesDto> categories) {
			this.categories = categories;
		}
	    
	    
}
