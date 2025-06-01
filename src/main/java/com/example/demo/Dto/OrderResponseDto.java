package com.example.demo.Dto;

import java.math.BigDecimal;

public class OrderResponseDto {
	private String orderId;
	private String orderDate;
	private String user;
	private String couponCode;
	private BigDecimal couponAmount;
	private BigDecimal totalAmount;
	public OrderResponseDto(String orderId, String orderDate, String user, String couponCode, BigDecimal couponAmount,
			BigDecimal totalAmount) {
		super();
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.user = user;
		this.couponCode = couponCode;
		this.couponAmount = couponAmount;
		this.totalAmount = totalAmount;
	}
	
	public OrderResponseDto() {
	
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public BigDecimal getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

}
