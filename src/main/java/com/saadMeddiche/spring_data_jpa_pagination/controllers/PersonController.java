package com.saadMeddiche.spring_data_jpa_pagination.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    @GetMapping
    public void personSummary() {

    }

}