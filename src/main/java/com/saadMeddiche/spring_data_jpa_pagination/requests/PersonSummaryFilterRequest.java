package com.saadMeddiche.spring_data_jpa_pagination.requests;

import org.springframework.util.StringUtils;
import java.time.LocalDate;

public record PersonSummaryFilterRequest(
        String fullName,
        LocalDate birthDateFrom,
        LocalDate birthDateTo
) {

    public String lowerCaseFullName() {
        if(!StringUtils.hasText(fullName)) return null;
        return fullName.toLowerCase();
    }

    public boolean noFilterValueIsPresent() {
        return lowerCaseFullName() == null && birthDateFrom == null && birthDateTo == null;
    }

}