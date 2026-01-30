package com.example.techgicus_ebilling.techgicus_ebilling.service;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.GstrDto;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class GstrBaseService {

    protected LocalDate parseFirstDay(String monthYear) {
        return YearMonth.parse(monthYear).atDay(1);
    }

    protected LocalDate parseLastDay(String monthYear) {
        return YearMonth.parse(monthYear).atEndOfMonth();
    }

    protected Company getCompany(Long companyId, CompanyRepository companyRepo) {
        return companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    protected GstrDto initReport(Company company,
                                 String startMonthYear,
                                 String endMonthYear) {
        GstrDto dto = new GstrDto();
        dto.setGstin(company.getGstin());
        dto.setLegalName(company.getBussinessName());
        dto.setPeriod(startMonthYear + " - " + endMonthYear);
        return dto;
    }

    protected String formatPeriod(LocalDate start, LocalDate end) {
        return start.getMonth() + " " + start.getYear() + " - " + end.getMonth() + " " + end.getYear();
    }
}
