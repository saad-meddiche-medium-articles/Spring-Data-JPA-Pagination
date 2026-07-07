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

    // ================= Using Page =================

    @GetMapping("/summary")
    public Page<PersonSummaryResponse> personSummary(Pageable pageable) {
        return this.personService.summary(pageable);
    }

    @GetMapping("/summary-custom-count")
    public Page<PersonSummaryResponse> summaryCustomCount(Pageable pageable) {
        return this.personService.summaryCustomCount(pageable);
    }

    @GetMapping("/improved-summary")
    public Page<PersonSummaryResponse> improvedSummary(Pageable pageable) {
        return this.personService.improvedSummary(pageable);
    }

    // ================= Using Slice =================

    @GetMapping("/slice")
    public Slice<PersonSummaryResponse> slice(Pageable pageable) {
        return this.personService.slice(pageable);
    }

}