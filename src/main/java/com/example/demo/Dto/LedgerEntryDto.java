package com.example.demo.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.model.PaymentType;

public class LedgerEntryDto {
	 private LocalDateTime date;
	    private PaymentType type;
	    private String OrderId;
	    private BigDecimal debit;
	    private BigDecimal credit;
	    private BigDecimal balance;
	    private String remarks;
		public LedgerEntryDto(LocalDateTime date, PaymentType type, String orderId, BigDecimal debit, BigDecimal credit,
				BigDecimal balance, String remarks) {
			super();
			this.date = date;
			this.type = type;
			OrderId = orderId;
			this.debit = debit;
			this.credit = credit;
			this.balance = balance;
			this.remarks = remarks;
		}
		
		public LedgerEntryDto() {
		}

		public LocalDateTime getDate() {
			return date;
		}
		public void setDate(LocalDateTime date) {
			this.date = date;
		}
		public PaymentType getType() {
			return type;
		}
		public void setType(PaymentType type) {
			this.type = type;
		}
		public String getOrderId() {
			return OrderId;
		}
		public void setOrderId(String orderId) {
			OrderId = orderId;
		}
		public BigDecimal getDebit() {
			return debit;
		}
		public void setDebit(BigDecimal debit) {
			this.debit = debit;
		}
		public BigDecimal getCredit() {
			return credit;
		}
		public void setCredit(BigDecimal credit) {
			this.credit = credit;
		}
		public BigDecimal getBalance() {
			return balance;
		}
		public void setBalance(BigDecimal balance) {
			this.balance = balance;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
	    
	    
}
