package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ContributionController {

    @GetMapping("/main/contribution")
    public String showMainContributionPage(){
        return "main-contribution_page";
    }
}
