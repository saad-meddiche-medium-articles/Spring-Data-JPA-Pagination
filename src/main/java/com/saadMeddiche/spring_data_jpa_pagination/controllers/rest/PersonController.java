package com.saadMeddiche.spring_data_jpa_pagination.controllers.rest;

import com.saadMeddiche.spring_data_jpa_pagination.requests.PersonSummaryFilterRequest;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import com.saadMeddiche.spring_data_jpa_pagination.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    // ================= Using Page =================

    @GetMapping("/page")
    public Page<PersonSummaryResponse> page_(Pageable pageable,
                                            @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.page_(pageable, filter);
    }

    @GetMapping("/page-custom-count")
    public Page<PersonSummaryResponse> page_customCount(Pageable pageable,
                                                       @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.page_customCount(pageable, filter);
    }

    @GetMapping("/improved-page")
    public Page<PersonSummaryResponse> page_improved(Pageable pageable,
                                                    @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.page_improved(pageable, filter);
    }

    @GetMapping("/estimated-page")
    public Page<PersonSummaryResponse> page_estimated(Pageable pageable,
                                                       @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.page_estimated(pageable, filter);
    }

    @GetMapping("/page-without-count")
    public Page<PersonSummaryResponse> page_withoutCount(Pageable pageable,
                                                        @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.page_withoutCount(pageable, filter);
    }

    // ================= Using Slice =================

    @GetMapping("/slice")
    public Slice<PersonSummaryResponse> slice(Pageable pageable,
                                              @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.slice(pageable, filter);
    }

    @GetMapping("/improved-slice")
    public Slice<PersonSummaryResponse> slice_improved(Pageable pageable,
                                                      @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.slice_improved(pageable, filter);
    }

    @GetMapping("/slice-without-count")
    public Slice<PersonSummaryResponse> slice_withoutCount(Pageable pageable,
                                                          @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.slice_withoutCount(pageable, filter);
    }

    // ================= Using Window =================

    @GetMapping("/window-manual-keyset")
    public Window<PersonSummaryResponse> window_manualKeyset(@RequestParam(required = false) Long lastSeenId,
                                                             @ModelAttribute PersonSummaryFilterRequest filter) {
        return this.personService.window_manualKeyset(lastSeenId, filter);
    }

    @GetMapping("/window-auto-keyset")
    public Window<PersonSummaryResponse> window_autoKeyset(@RequestParam(required = false) Long lastSeenId) {
        return this.personService.window_autoKeyset(lastSeenId);
    }

}
