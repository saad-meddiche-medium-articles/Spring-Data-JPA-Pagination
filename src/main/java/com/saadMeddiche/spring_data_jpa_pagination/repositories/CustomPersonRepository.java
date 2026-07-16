package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;

import java.util.Collection;
import java.util.List;

public interface CustomPersonRepository {

    List<PersonSummaryResponse> findSummaryByIds(Collection<Long> personIds);

}