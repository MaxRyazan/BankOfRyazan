package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.ExchangeRateClassService;
import ru.maxryazan.bankofryazan.service.RateService;

@Controller
public class BankMainPageController {
    private final ExchangeRateClassService exchangeRate;
    private final RateService rateService;


    public BankMainPageController(ExchangeRateClassService exchangeRate, RateService rateService) {
        this.exchangeRate = exchangeRate;
        this.rateService = rateService;
    }

    @GetMapping("/")
    public String toMainPage(){
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String showMainPage(Model model) {
        try {
            ExchangeRateClass exchangeRateClass = exchangeRate.getRateFromAPI();
            Rate metals = rateService.getRateFromAPI();
                System.out.println(metals);
            model.addAttribute("USD", exchangeRateClass.getCourse_USD());
            model.addAttribute("EUR", exchangeRateClass.getCourse_EUR());
        } catch (Exception e){
            throw  new RuntimeException();
        }
        return "main-page";
    }

    @GetMapping("/main/credit")
    public String showCreditPage(){
        return "credit-page";
        }


    @GetMapping("/main/insurance")
    public String showInsurancePage(){
        return "insurance-page";
    }

    @GetMapping("/main/cell-rental")
    public String cellRentalPage(){
        return "cell_rental-page";
    }

    @GetMapping("/main/investments")
    public String investmentsPage(){
        return "investments-page";
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
