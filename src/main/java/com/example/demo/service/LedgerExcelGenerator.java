package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.LedgerEntryDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Service
public class LedgerExcelGenerator implements ReportGenerator {

	

	@Override
	public byte[] generate(List<LedgerEntryDto> entries) throws IOException {
		// TODO Auto-generated method stub
        ByteArrayOutputStream out = new ByteArrayOutputStream();

		 try (Workbook workbook = new XSSFWorkbook()) {

	            Sheet sheet = workbook.createSheet("Ledger Book");

	            // Header row
	            Row header = sheet.createRow(0);
	            String[] columns = {"Date", "Type", "OrderId", "Debit", "Credit", "Balance", "Remarks"};
	            for (int i = 0; i < columns.length; i++) {
	                header.createCell(i).setCellValue(columns[i]);
	            }

	            // Data rows
	            int rowNum = 1;
	            for (LedgerEntryDto entry : entries) {
	                Row row = sheet.createRow(rowNum++);
	                row.createCell(0).setCellValue(entry.getDate().toString());
	                row.createCell(1).setCellValue(entry.getType().name());
	                row.createCell(2).setCellValue(entry.getOrderId());
	                row.createCell(3).setCellValue(entry.getDebit() != null ? entry.getDebit().doubleValue() : 0);
	                row.createCell(4).setCellValue(entry.getCredit() != null ? entry.getCredit().doubleValue() : 0);
	                row.createCell(5).setCellValue(entry.getBalance().doubleValue());
	                row.createCell(6).setCellValue(entry.getRemarks());
	            }

	            workbook.write(out); // Don't close 'out' here
	        }
		 return out.toByteArray();
	}

	
}
