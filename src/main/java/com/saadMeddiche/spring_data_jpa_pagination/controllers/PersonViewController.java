package com.saadMeddiche.spring_data_jpa_pagination.controllers;

import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import com.saadMeddiche.spring_data_jpa_pagination.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/persons/view")
@RequiredArgsConstructor
public class PersonViewController {

    private final PersonService personService;

    // Default landing: redirect to the Page solution
    @GetMapping
    public String defaultView() {
        return "redirect:/persons/view/page";
    }

    // Page-based pagination with multiple variants
    // Exposes 1-based page numbers in the UI while keeping 0-based indexes internally
    @GetMapping("/page")
    public String page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "improvedSummary") String variant,
            Model model
    ) {
        int zeroBasedPage = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(zeroBasedPage, size);

        Page<PersonSummaryResponse> summaryPage = switch (variant) {
            case "summaryCustomCount" -> personService.summaryCustomCount(pageable);
            case "improvedSummary" -> personService.improvedSummary(pageable);
            case "summaryWithoutCount" -> personService.summaryWithoutCount(pageable);
            case "summary" -> personService.summary(pageable);
            default -> personService.summary(pageable);
        };

        int totalPages = summaryPage.getTotalPages();
        int currentPage = summaryPage.getNumber(); // 0-based
        int maxPagesToShow = 5;

        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        if (endPage - startPage + 1 < maxPagesToShow) {
            if (startPage == 0) {
                endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);
            } else if (endPage == totalPages - 1) {
                startPage = Math.max(0, endPage - maxPagesToShow + 1);
            }
        }

        model.addAttribute("page", summaryPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("variant", variant);
        return "page";
    }

    // Slice-based pagination with multiple variants
    @GetMapping("/slice")
    public String slice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sliceWithoutCount") String variant,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Slice<PersonSummaryResponse> slice = switch (variant) {
            case "slice" -> personService.slice(pageable);
            case "improvedSlice" -> personService.improvedSlice(pageable);
            case "sliceWithoutCount" -> personService.sliceWithoutCount(pageable);
            default -> personService.sliceWithoutCount(pageable);
        };

        model.addAttribute("slice", slice);
        model.addAttribute("variant", variant);
        return "slice";
    }

    // Window / keyset-based pagination (variant parameter reserved for future versions)
    @GetMapping("/window")
    public String window(
            @RequestParam(required = false) Long lastSeenId,
            @RequestParam(defaultValue = "default") String variant,
            Model model
    ) {
        Window<PersonSummaryResponse> window = personService.window(lastSeenId);
        List<PersonSummaryResponse> content = window.getContent();

        boolean hasNext = window.hasNext();
        // Spring Data Window is forward-only; no built-in hasPrevious support
        boolean hasPrev = false;

        Long nextCursor = (hasNext && !content.isEmpty())
                ? content.get(content.size() - 1).id()
                : null;

        Long prevCursor = null;

        model.addAttribute("items", content);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("nextCursor", nextCursor);
        model.addAttribute("prevCursor", prevCursor);
        model.addAttribute("variant", variant);
        return "window";
    }
}
