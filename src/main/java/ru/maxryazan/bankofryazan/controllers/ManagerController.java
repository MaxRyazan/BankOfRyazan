package ru.maxryazan.bankofryazan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ContributionService;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.InvestmentService;


@Controller
public class ManagerController {

    private final ClientService clientService;
    private final CreditService creditService;
    private final ContributionService contributionService;
    private final InvestmentService investmentService;

    @Autowired
    public ManagerController(ClientService clientService, CreditService creditService, ContributionService contributionService, InvestmentService investmentService) {
        this.clientService = clientService;
        this.creditService = creditService;
        this.contributionService = contributionService;
        this.investmentService = investmentService;
    }

    @GetMapping("/manager")
    public String showManagerPage(){
        return "manager/manager-main-page";
    }

    @GetMapping("/manager/new-client")
    public String getAddNewClient(){
        System.out.println("get");
        return "manager/new-client";
    }

    @PostMapping("/manager/new-client")
    public String postAddNewClient(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam String phoneNumber){
        System.out.println("post");
        clientService.save(firstName, lastName, phoneNumber);
        return "redirect:/manager";
    }

    @GetMapping("manager/credit")
    public String getNewCredit(){
        return "manager/new-credit";
    }

    @PostMapping("/manager/credit")
    public String postNewCredit(@RequestParam String phoneNumber,
                                @RequestParam int sumOfCredit,
                                @RequestParam double percentOfCredit,
                                @RequestParam int numberOfPays){
        creditService.addNewCredit(phoneNumber, sumOfCredit, percentOfCredit, numberOfPays);
        return "redirect:/manager/credit";
    }

    @GetMapping("/manager/contribution")
    public String getNewContribution(){
        return "manager/new-contribution";
    }

    @PostMapping("/manager/contribution")
    public String postNewContribution(@RequestParam String phoneNumber,
                                      @RequestParam double sum,
                                      @RequestParam double percent,
                                      @RequestParam int duration) {
        contributionService.addNewContribution(phoneNumber, sum, percent, duration);
        return "redirect:/manager/contribution";
    }

}
