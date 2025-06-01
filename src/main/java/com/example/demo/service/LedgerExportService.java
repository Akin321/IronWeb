package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Dto.LedgerEntryDto;

@Service
public class LedgerExportService {

    private final LedgerService ledgerService;
    private final ReportGenerator excelGenerator;

    public LedgerExportService(
        LedgerService ledgerService,
        LedgerExcelGenerator excelGenerator 
    ) {
        this.ledgerService = ledgerService;
        this.excelGenerator = excelGenerator;
    }

    public byte[] exportLedger(LocalDate start, LocalDate end) throws IOException {
        List<LedgerEntryDto> entries = ledgerService.getLedger(start, end);
        return excelGenerator.generate(entries);
    }
}
