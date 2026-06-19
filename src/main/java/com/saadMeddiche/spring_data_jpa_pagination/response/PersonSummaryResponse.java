package com.saadMeddiche.spring_data_jpa_pagination.response;

public record PersonSummaryResponse(
        long id,
        String firstName,
        String lastName,
        long productCount
) {}