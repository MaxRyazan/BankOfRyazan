package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.service.ContributionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ContributionController {

//    private final ContributionService contributionService;
//
//    public ContributionController(ContributionService contributionService) {
//        this.contributionService = contributionService;
//    }

    @GetMapping("/main/contribution")
    public String showMainContributionPage(){
        return "main-contribution_page";
    }

//    @GetMapping("/main/contribution/make-contribution")
//    public String makeContribution(){
//        return "make-contribution_page";
//    }
//
//    @PostMapping("/main/contribution/make-contribution")
//    public String postMakeContribution(@RequestParam String phoneNumber,
//                                       @RequestParam double sum,
//                                       @RequestParam double percent,
//                                       @RequestParam int duration){
//        contributionService.addNewContribution(phoneNumber, sum, percent, duration);
//        return "redirect:/main/personal-area";
//    }
}
