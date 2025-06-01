package com.example.demo.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.example.demo.Dto.ReportData;

@Service
public class ThymeleafPdfGenerator  implements PdfGenerator {
    private final TemplateEngine templateEngine;

    public ThymeleafPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

	@Override
	public byte[] generatePdf(ReportData data) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> model = new HashMap<>();
        model.put("startDate", data.getStartDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        model.put("endDate", data.getEndDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        model.put("generatedDate", data.getGeneratedDate());
        model.put("totalOrders", data.getTotalOrders());
        model.put("totalSalesAmount", data.getTotalSalesAmount());
        model.put("totalDiscount", data.getTotalDiscount());
        model.put("couponDiscount", data.getCouponDiscount());
        model.put("salesChart", data.getSalesChartBase64());
        model.put("topProducts", data.getTopProducts());
        model.put("categories", data.getCategories());

        return ReportPdfGenerator.generatePdfFromHtml(model, templateEngine);
	}



}
