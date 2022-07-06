package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxryazan.bankofryazan.jobs.MoneyParser;

@Controller
public class BankMainPageController {
    private final MoneyParser parser;


    public BankMainPageController(MoneyParser parser) {
        this.parser = parser;
    }

    @GetMapping("/")
    public String toMainPage(){
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String showMainPage(Model model) {
        try {
            MoneyParser moneyParser = parser.someMethod();
            model.addAttribute("USD", moneyParser.getCourse_USD());
            model.addAttribute("EUR", moneyParser.getCourse_EUR());
        } catch (Exception e){
            throw  new RuntimeException();
        }
        return "main-page";
    }

    @GetMapping("/main/credit")
    public String showCreditPage(){
        return "credit-page";
        }

    @GetMapping("/main/contribution")  //вклад
    public String showContributionPage(){
        return "contribution-page";
    }

    @GetMapping("/main/insurance")
    public String showInsurancePage(){
        return "insurance-page";
    }

    @GetMapping("/main/cell-rental")
    public String cellRentalPage(){
        return "cell_rental-page";
    }

    @GetMapping("/main/precision-metal")
    public String preciousMetalPage(){
        return "precision_metal-page";
    }

    @GetMapping("/main/feedback")
    public String feedbackPage(){
        return "feedback-page";
    }

    @GetMapping("/main/request-a-call")
    public String requestACallPage(){
        return "request_a_call-page";
    }

}
