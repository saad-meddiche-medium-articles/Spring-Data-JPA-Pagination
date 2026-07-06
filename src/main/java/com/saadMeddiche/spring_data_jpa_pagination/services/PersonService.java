package com.saadMeddiche.spring_data_jpa_pagination.services;

import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Page<PersonSummaryResponse> summaryCustomCount(Pageable pageable) {
        return personRepository.summaryCustomCount(pageable);
    }

    public Page<PersonSummaryResponse> improvedSummary(Pageable pageable) {

        Pageable pageableWithForcedSort = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").ascending()
        );

        List<Long> personIds = personRepository.improvedSummary(pageableWithForcedSort);

        List<PersonSummaryResponse> content = personRepository.improvedSummary(personIds, pageableWithForcedSort.getSort());

        long totalPersons = personRepository.count();

        return new PageImpl<>(content, pageableWithForcedSort , totalPersons);

    }

}