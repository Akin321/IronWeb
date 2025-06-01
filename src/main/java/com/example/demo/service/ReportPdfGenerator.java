package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class ReportPdfGenerator {

	public static byte[] generatePdfFromHtml(Map<String, Object> data, TemplateEngine templateEngine) throws IOException {
		// TODO Auto-generated method stub
		  Context context = new Context();
	        context.setVariables(data);

	        String htmlContent = templateEngine.process("report", context); // 'report.html' in templates/

	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocumentFromString(htmlContent);
	        renderer.layout();

	        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	            renderer.createPDF(baos);
	            return baos.toByteArray();
	        }
	    }
	}


