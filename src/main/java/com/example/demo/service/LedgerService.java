package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Dto.LedgerEntryDto;

@Service
public class LedgerService {

    private final SalesService salesService;

    public LedgerService(SalesService salesService) {
        this.salesService = salesService;
    }

    public List<LedgerEntryDto> getLedger(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
        return salesService.getAdminLedger(startDateTime, endDateTime);
    }
}
