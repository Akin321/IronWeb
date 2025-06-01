package com.example.demo.controller;

import java.io.ByteArrayOutputStream;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;

import com.example.demo.Dto.CategorySalesDto;
import com.example.demo.Dto.LedgerEntryDto;
import com.example.demo.Dto.OrderResponseDto;
import com.example.demo.Dto.ReportData;
import com.example.demo.Dto.TopProductDto;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderModel;
import com.example.demo.model.OrderStatus;
import com.example.demo.model.ProductModel;
import com.example.demo.model.ProductTypes;
import com.example.demo.service.LedgerExcelGenerator;
import com.example.demo.service.LedgerExportService;
import com.example.demo.service.PdfGenerator;
import com.example.demo.service.ReportPdfGenerator;
import com.example.demo.service.ReportService;
import com.example.demo.service.SalesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("admin")
public class SalesReportController {
	@Autowired
	SalesService salesService;
	
	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	LedgerExcelGenerator ledgerExcelGenerator;
	
	@Autowired
	ReportService reportService;
	
	@Autowired
	PdfGenerator pdfGenerator;
	
	@Autowired
	LedgerExportService ledgerExportService;
	 
	@GetMapping("/dashboard")
	public String SalesReport(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size,Model model,
			HttpServletRequest request,RedirectAttributes redirectAttributes,HttpSession session) {
		
		try {
			 if (session.getAttribute("jwttoken") == null) {
			        return "redirect:/admin/login";
			    }
			 LocalDate endDate = LocalDate.now();
			 LocalDate startDate = endDate.minusDays(6);
			 LocalDateTime startDateTime = startDate.atStartOfDay();
			 LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
			List<OrderModel> allOrders = salesService.getOrders(startDateTime, endDateTime);
			long totalOrders=allOrders.size();
			BigDecimal totalSalesAmount = salesService.getTotalSales(startDateTime,endDateTime);
			BigDecimal totalDiscount = allOrders.stream()
					.filter(order->order.getOrderItems().stream().noneMatch(item->item.getOrderStatus()== OrderStatus.Cancelled || item.getOrderStatus() == OrderStatus.Returned))
				    .flatMap(order -> order.getOrderItems().stream())
				    .map(item -> item.getTotalDiscount() != null ? item.getTotalDiscount() : BigDecimal.ZERO)
				    .reduce(BigDecimal.ZERO, BigDecimal::add);
			
			BigDecimal couponDeduction = allOrders.stream()
				    .map(order -> order.getCouponAmount() != null ? order.getCouponAmount() : BigDecimal.ZERO)
				    .reduce(BigDecimal.ZERO, BigDecimal::add);

			List<TopProductDto> topProducts = salesService.getTopSellingProducts(startDateTime, endDateTime);
        Map<String, Double> salesByDate = salesService.getSalesGroupedByDate(startDateTime, endDateTime);
		List<CategorySalesDto> categories=salesService.getCategories(startDateTime, endDateTime);

        ObjectMapper mapper = new ObjectMapper();
        String salesByDateJson = mapper.writeValueAsString(salesByDate);

	    
	    model.addAttribute("totalOrders",totalOrders);
	    model.addAttribute("totalSalesAmount",totalSalesAmount);
	    model.addAttribute("totalDiscount",totalDiscount);
	    model.addAttribute("couponDiscount",couponDeduction);
	    model.addAttribute("topProducts", topProducts);
	    model.addAttribute("categories",categories);
	    model.addAttribute("salesByDate", salesByDateJson);
		return "salesReport";
		}
		catch(Exception e) {
			e.printStackTrace();
			String referer=request.getHeader("referer");
			redirectAttributes.addFlashAttribute("errorMessage","Something happened while visiting Dashboard");
			return "redirect:"+(referer!=null ? referer:"/admin/login");
		}
	}
	
	@PostMapping("/filter/sales")
	@ResponseBody
	public ResponseEntity<?> filterReport(@RequestParam LocalDate startDate,@RequestParam LocalDate endDate,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size) {
		try {   
			   if (startDate.isAfter(endDate) || endDate.isAfter(LocalDate.now())) {
				   return ResponseEntity
					        .badRequest()
					        .body("Start date must be before or equal to end date.");
			    }
		LocalDateTime startDateTime = startDate.atStartOfDay();
		   LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
			List<OrderModel> allOrders = salesService.getOrders(startDateTime, endDateTime);

			long totalOrders=allOrders.size();
			BigDecimal totalSalesAmount = salesService.getTotalSales(startDateTime,endDateTime);	
			BigDecimal totalDiscount = allOrders.stream()
				    .flatMap(order -> order.getOrderItems().stream())
				    .map(item -> item.getTotalDiscount() != null ? item.getTotalDiscount() : BigDecimal.ZERO)
				    .reduce(BigDecimal.ZERO, BigDecimal::add);
			
			BigDecimal couponDeduction = allOrders.stream()
				    .map(order -> order.getCouponAmount() != null ? order.getCouponAmount() : BigDecimal.ZERO)
				    .reduce(BigDecimal.ZERO, BigDecimal::add);

			List<TopProductDto> topProducts = salesService.getTopSellingProducts(startDateTime, endDateTime);
			List<CategorySalesDto> categories=salesService.getCategories(startDateTime, endDateTime);
        Map<String, Double> salesByDate = salesService.getSalesGroupedByDate(startDateTime, endDateTime);

			Map<String,Object> response=new HashMap<>();
			response.put("totalOrders",totalOrders);
			response.put("totalDiscount",totalDiscount);
			response.put("couponDiscount", couponDeduction);
		    response.put("totalSalesAmount",totalSalesAmount);
		    response.put("salesByDate", salesByDate);
		    response.put("topProducts", topProducts);
		    response.put("categories",categories);

	

		    return ResponseEntity.ok(response);

		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	
	}
	

    @PostMapping("/generateReport")
    @ResponseBody
    public ResponseEntity<byte[]> generateReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) throws Exception {

        ReportData data = reportService.generateReportData(startDate, endDate);
        byte[] pdfBytes = pdfGenerator.generatePdf(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("SalesReport.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);


    }
    @PostMapping("/generateLedgerBook")
    @ResponseBody
    public ResponseEntity<?> generateLedger(  @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){
    	  try {
    		  
    	        byte[] excelBytes = ledgerExportService.exportLedger(startDate, endDate);


    	        ByteArrayResource resource = new ByteArrayResource(excelBytes);

    	        HttpHeaders headers = new HttpHeaders();
    	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=admin-ledger.xlsx");
    	        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    	        headers.setContentLength(resource.contentLength());

    	        return ResponseEntity
    	                .ok()
    	                .headers(headers)
    	                .body(resource);
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return ResponseEntity
    	                .status(500)
    	                .body("Failed to generate Excel: " + e.getMessage());
    	    }
    }
	
	
}
