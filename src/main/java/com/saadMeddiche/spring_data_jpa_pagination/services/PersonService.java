package com.saadMeddiche.spring_data_jpa_pagination.services;

import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public Page<PersonSummaryResponse> summary(Pageable pageable) {
        return personRepository.summary(pageable);
    }

    public Slice<PersonSummaryResponse> slice(Pageable pageable) {
        return personRepository.slice(pageable);
    }

}