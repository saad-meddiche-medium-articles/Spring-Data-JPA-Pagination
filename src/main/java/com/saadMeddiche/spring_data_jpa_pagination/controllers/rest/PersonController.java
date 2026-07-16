package com.saadMeddiche.spring_data_jpa_pagination.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.web.bind.annotation.*;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import com.saadMeddiche.spring_data_jpa_pagination.services.PersonService;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    // ================= Using Page =================

    @GetMapping("/page")
    public Page<PersonSummaryResponse> page_(Pageable pageable) {
        return this.personService.page_(pageable);
    }

    @GetMapping("/page-custom-count")
    public Page<PersonSummaryResponse> page_customCount(Pageable pageable) {
        return this.personService.page_customCount(pageable);
    }

    @GetMapping("/improved-page")
    public Page<PersonSummaryResponse> page_improved(Pageable pageable) {
        return this.personService.page_improved(pageable);
    }

    @GetMapping("/page-without-count")
    public Page<PersonSummaryResponse> page_withoutCount(Pageable pageable) {
        return this.personService.page_withoutCount(pageable);
    }

    // ================= Using Slice =================

    @GetMapping("/slice")
    public Slice<PersonSummaryResponse> slice(Pageable pageable) {
        return this.personService.slice(pageable);
    }

    @GetMapping("/improved-slice")
    public Slice<PersonSummaryResponse> slice_improved(Pageable pageable) {
        return this.personService.slice_improved(pageable);
    }

    @GetMapping("/slice-without-count")
    public Slice<PersonSummaryResponse> slice_withoutCount(Pageable pageable) {
        return this.personService.slice_withoutCount(pageable);
    }

    // ================= Using Window =================

    @GetMapping("/window")
    public Window<PersonSummaryResponse> window(@RequestParam(required = false) Long lastSeenId) {
        return this.personService.window(lastSeenId);
    }

}