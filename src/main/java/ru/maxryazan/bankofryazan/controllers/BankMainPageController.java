package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ExchangeRateClassService;
import ru.maxryazan.bankofryazan.service.RateService;

@Controller
public class BankMainPageController {
    private final ExchangeRateClassService exchangeRate;
    private final RateService rateService;
    private final ClientService clientService;

    public BankMainPageController(ExchangeRateClassService exchangeRate, RateService rateService, ClientService clientService) {
        this.exchangeRate = exchangeRate;
        this.rateService = rateService;
        this.clientService = clientService;
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

    @GetMapping("main/registration")
    public String getSelfRegistration(){
       return "personal/self-registration-page";
    }

    @PostMapping("main/registration")
    public String postSelfRegistration(@RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String phoneNumber,
                                       @RequestParam String email,
                                       @RequestParam String pinCode){
        clientService.selfRegistration(firstName, lastName, phoneNumber, email, pinCode);
        return "redirect:/main/personal-area";
    }
}
