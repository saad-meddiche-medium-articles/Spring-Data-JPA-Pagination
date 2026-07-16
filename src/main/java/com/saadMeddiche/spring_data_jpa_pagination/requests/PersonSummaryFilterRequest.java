package com.saadMeddiche.spring_data_jpa_pagination.requests;

import java.time.LocalDate;

public record PersonSummaryFilterRequest(
        String fullName,
        LocalDate birthDateFrom,
        LocalDate birthDateTo
) {}