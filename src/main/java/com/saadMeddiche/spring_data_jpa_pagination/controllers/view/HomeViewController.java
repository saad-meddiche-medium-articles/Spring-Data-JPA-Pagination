package com.saadMeddiche.spring_data_jpa_pagination.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeViewController {

    @GetMapping
    public String defaultView() {
        return "redirect:/persons/view/window";
    }

}