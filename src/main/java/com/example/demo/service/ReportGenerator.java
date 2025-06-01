package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import com.example.demo.Dto.LedgerEntryDto;

public interface ReportGenerator {
    byte[] generate(List<LedgerEntryDto> entries) throws IOException;
}
