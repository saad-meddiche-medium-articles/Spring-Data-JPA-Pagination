package com.saadMeddiche.spring_data_jpa_pagination.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import com.saadMeddiche.spring_data_jpa_pagination.services.PersonService;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/summary")
    public Page<PersonSummaryResponse> personSummary(Pageable pageable) {
        return this.personService.summary(pageable);
    }

    @GetMapping("/slice")
    public Slice<PersonSummaryResponse> personSlice(Pageable pageable) {
        return this.personService.slice(pageable);
    }

    @GetMapping("/improved-summary")
    public Slice<PersonSummaryResponse> improvedSummary(Pageable pageable) {
        return this.personService.improvedSummary(pageable);
    }

}