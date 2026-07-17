package com.saadMeddiche.spring_data_jpa_pagination.response;

import java.time.LocalDate;

public record PersonSummaryResponse(
        long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        long productCount
) {}