package com.example.demo.service;

import com.example.demo.Dto.ReportData;

public interface PdfGenerator {
    byte[] generatePdf(ReportData data) throws Exception;

}
