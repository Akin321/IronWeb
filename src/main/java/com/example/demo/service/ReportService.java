package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;


import com.example.demo.Dto.ReportData;



	public interface ReportService {
	    ReportData generateReportData(LocalDate startDate, LocalDate endDate) throws IOException;
	}


