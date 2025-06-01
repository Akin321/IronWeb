package com.example.demo.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.Dto.CategorySalesDto;
import com.example.demo.Dto.ReportData;
import com.example.demo.Dto.TopProductDto;
import com.example.demo.model.OrderModel;

@Service
public class ReportServiceImplementation implements ReportService{
    private final SalesService salesService;


		    public ReportServiceImplementation(SalesService salesService) {
		        this.salesService = salesService;
		    }

		    @Override
		    public ReportData generateReportData(LocalDate startDate, LocalDate endDate) throws IOException {
		        LocalDateTime startDateTime = startDate.atStartOfDay();
		        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

		        List<OrderModel> allOrders = salesService.getOrders(startDateTime, endDateTime);
		        long totalOrders = allOrders.size();
		        BigDecimal totalSalesAmount = salesService.getTotalSales(startDateTime, endDateTime);

		        BigDecimal totalDiscount = allOrders.stream()
		            .flatMap(order -> order.getOrderItems().stream())
		            .map(item -> item.getTotalDiscount() != null ? item.getTotalDiscount() : BigDecimal.ZERO)
		            .reduce(BigDecimal.ZERO, BigDecimal::add);

		        BigDecimal couponDeduction = allOrders.stream()
		            .map(order -> order.getCouponAmount() != null ? order.getCouponAmount() : BigDecimal.ZERO)
		            .reduce(BigDecimal.ZERO, BigDecimal::add);

		        List<TopProductDto> topProducts = salesService.getTopSellingProducts(startDateTime, endDateTime);
		        List<CategorySalesDto> categories = salesService.getCategories(startDateTime, endDateTime);
		        Map<String, Double> salesByDate = salesService.getSalesGroupedByDate(startDateTime, endDateTime);
		        String base64Chart = salesService.generateSalesChart(salesByDate);

		        ReportData data = new ReportData();
		        data.setStartDate(startDate);
		        data.setEndDate(endDate);
		        data.setGeneratedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
		        data.setTotalOrders(totalOrders);
		        data.setTotalSalesAmount(totalSalesAmount);
		        data.setTotalDiscount(totalDiscount);
		        data.setCouponDiscount(couponDeduction);
		        data.setSalesChartBase64(base64Chart);
		        data.setTopProducts(topProducts);
		        data.setCategories(categories);

		        return data;	}
	
}
