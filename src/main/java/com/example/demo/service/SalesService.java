package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.CategorySalesDto;
import com.example.demo.Dto.LedgerEntryDto;
import com.example.demo.Dto.OrderResponseDto;
import com.example.demo.Dto.TopProductDto;
import com.example.demo.Repository.OrderItemRepo;
import com.example.demo.Repository.OrderRepo;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderModel;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.PaymentType;
import com.example.demo.model.ProductModel;
import com.example.demo.model.ProductTypes;
import com.razorpay.Product;



@Service
public class SalesService {
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	OrderItemRepo orderItemRepo;


	public List<TopProductDto> getTopSellingProducts(LocalDateTime startDate, LocalDateTime endDate) {
	    List<OrderItem> orderItems = orderItemRepo.findByCreatedAtBetween(startDate, endDate);
	    Map<ProductModel, Integer> productSales = new HashMap<>();

	    for (OrderItem item : orderItems) {
	        ProductModel product = item.getProduct();
	        int quantity = item.getQuantity();
	        productSales.put(product, productSales.getOrDefault(product, 0) + quantity);
	    }

	    // Convert to sorted list of TopProductDto
	    List<TopProductDto> top3Products = productSales.entrySet()
	        .stream()
	        .sorted(Map.Entry.<ProductModel, Integer>comparingByValue().reversed())
	        .limit(10)
	        .map(entry -> new TopProductDto(entry.getKey(), entry.getValue()))
	        .collect(Collectors.toList());

	    return top3Products;
	}


	public Map<String, Double> getSalesGroupedByDate(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		List<OrderModel> orders=orderRepo.findByOrderDateBetween(startDate, endDate);
		Map<String, Double> dailyNetSales = orders.stream()
			    .collect(Collectors.groupingBy(
			        order -> order.getOrderDate(), // Grouping by just the date part
			        TreeMap::new,
			        Collectors.summingDouble(order -> {
			            BigDecimal total = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;

			            // Subtract cancelled or returned item amounts
			            BigDecimal deduction = order.getOrderItems().stream()
			                .filter(item -> item.getOrderStatus() == OrderStatus.Cancelled || item.getOrderStatus() == OrderStatus.Returned ||item.getOrderStatus()==OrderStatus.PaymentFailed)
			                .map(item -> item.getTotalAmount() != null ? item.getTotalAmount() : BigDecimal.ZERO)
			                .reduce(BigDecimal.ZERO, BigDecimal::add);

			            return total.subtract(deduction).doubleValue();
			        })
			    		 ));
			  
		return dailyNetSales;
	}

	public List<OrderResponseDto> convertToDto(Page<OrderModel> orderspf) {
		// TODO Auto-generated method stub
		List<OrderResponseDto> orderResponse = new ArrayList<>();

	    List<OrderModel> orders = orderspf.getContent();
	    for (OrderModel order : orders) {
	        OrderResponseDto response = new OrderResponseDto();
	        response.setCouponAmount(order.getCouponAmount());
	        response.setCouponCode(order.getCouponCode());
	        response.setOrderDate(order.getOrderDate());
	        response.setOrderId(order.getOrderId());
	        response.setTotalAmount(order.getTotalAmount());
	        response.setUser(order.getUser().getEmail());

	        orderResponse.add(response); // ✅ Add to list
	    }

	    return orderResponse;
	}

	public List<OrderModel> getOrders(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		List<OrderModel> orders=orderRepo.findByOrderDateBetween(startDate, endDate);

		return orders;
	}

	public List<CategorySalesDto> getCategories(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		// TODO Auto-generated method stub
	    List<OrderItem> orderItems = orderItemRepo.findByCreatedAtBetween(startDateTime, endDateTime);
	    
	    Map<ProductTypes, Integer> catSales = new HashMap<>();

	    for (OrderItem item : orderItems) {
	    	ProductTypes category=item.getProduct().getProductType();
	        int quantity = item.getQuantity();
	        catSales.put(category, catSales.getOrDefault(category, 0) + quantity);
	    }

	    
	    return catSales.entrySet().stream()
	            .sorted(Map.Entry.<ProductTypes, Integer>comparingByValue().reversed())
	            .limit(10) // Get top 3
	            .map(entry -> {
	                ProductTypes type = entry.getKey();
	                int sold = entry.getValue();
	                return new CategorySalesDto(type.getName(), type.getImage(), sold);
	            })
	            .collect(Collectors.toList());

		
	}

	   public  String generateSalesChart(Map<String, Double> salesByDate) throws IOException{
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	        salesByDate.entrySet().stream()
	                .sorted(Map.Entry.comparingByKey())
	                .forEach(entry -> dataset.addValue(entry.getValue(), "Sales", entry.getKey()));

	        JFreeChart barChart = ChartFactory.createBarChart(
	                "Sales by Date",
	                "Date",
	                "Total Sales",
	                dataset,
	                PlotOrientation.VERTICAL,
	                false, true, false
	        );

	        BufferedImage chartImage = barChart.createBufferedImage(800, 400);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(chartImage, "png", baos);
	        return Base64.getEncoder().encodeToString(baos.toByteArray());
	    }


	public List<LedgerEntryDto> getAdminLedger(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		List<LedgerEntryDto> book=new ArrayList<>();
		List<OrderModel> orders=orderRepo.findByOrderDateBetween(startDate, endDate);
	    BigDecimal runningBalance = BigDecimal.ZERO;

		for(OrderModel order:orders) {
			LedgerEntryDto dto=new LedgerEntryDto();
	        runningBalance = runningBalance.add(order.getTotalAmount());

			dto.setBalance(runningBalance);
			dto.setCredit(order.getTotalAmount());
			dto.setDate(order.getcorrectOrderDate());
			dto.setOrderId(order.getOrderId());
			dto.setRemarks("credited order amount");
			dto.setType(order.getPaymentType());
			book.add(dto);
			for(OrderItem item:order.getOrderItems()) {
				
				if(item.getOrderStatus()==OrderStatus.PaymentFailed) {
					LedgerEntryDto itemdto=new LedgerEntryDto();
	                runningBalance = runningBalance.subtract(item.getTotalAmount());

					itemdto.setBalance(runningBalance);
					itemdto.setDebit(item.getTotalAmount());
					itemdto.setDate(order.getcorrectOrderDate());
					itemdto.setOrderId(order.getOrderId());
					itemdto.setRemarks("payment failed for the item");
					itemdto.setType(order.getPaymentType());
					book.add(itemdto);
				}
				
				if(item.getOrderStatus()==OrderStatus.Cancelled) {
					LedgerEntryDto itemdto=new LedgerEntryDto();
	                runningBalance = runningBalance.subtract(item.getTotalAmount());

					itemdto.setBalance(runningBalance);
					itemdto.setDebit(item.getTotalAmount());
					itemdto.setDate(item.getCorrectCancelDate());
					itemdto.setOrderId(order.getOrderId());
					itemdto.setRemarks("debited cancelled order amount");
					itemdto.setType(order.getPaymentType());
					book.add(itemdto);
				}
				if(item.getOrderStatus()==OrderStatus.Returned) {
					LedgerEntryDto reitemdto=new LedgerEntryDto();
	                runningBalance = runningBalance.subtract(item.getTotalAmount());

					reitemdto.setBalance(runningBalance);
					reitemdto.setDebit(item.getTotalAmount());
					reitemdto.setDate(item.getReturnRequest().getCorrectApprovedDate());
					reitemdto.setOrderId(order.getOrderId());
					reitemdto.setRemarks("debited returned order amount");
					reitemdto.setType(order.getPaymentType());
					book.add(reitemdto);
				}
			}
			
		}
	  

	    book.sort(Comparator.comparing(LedgerEntryDto::getDate));

		return book;
	}


	public BigDecimal getTotalSales(LocalDateTime startDate, LocalDateTime endDate) {
		// TODO Auto-generated method stub
		List<OrderModel> orders=orderRepo.findByOrderDateBetween(startDate, endDate);
	    BigDecimal runningBalance = BigDecimal.ZERO;

		for(OrderModel order:orders) {
	        runningBalance = runningBalance.add(order.getTotalAmount());
	        for(OrderItem item:order.getOrderItems()) {
				if(item.getOrderStatus()==OrderStatus.Cancelled || item.getOrderStatus()==OrderStatus.Returned ||item.getOrderStatus()==OrderStatus.PaymentFailed)  {
	                runningBalance = runningBalance.subtract(item.getTotalAmount());
				}
	        }
		}

		
		return runningBalance;
	}
	public BigDecimal getCouponDiscount(LocalDateTime startDate, LocalDateTime endDate) {
		  return orderRepo.findByOrderDateBetween(startDate, endDate).stream()
			        .map(order -> order.getCouponAmount() != null ? order.getCouponAmount() : BigDecimal.ZERO)
			        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
}


	    
