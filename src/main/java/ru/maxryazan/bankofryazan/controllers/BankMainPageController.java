package ru.maxryazan.bankofryazan.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.*;

@Controller
public class BankMainPageController {
    private final ExchangeRateClassService exchangeRate;
    private final RateService rateService;
    private final ClientService clientService;
    private final MailSender mailSender;

    public BankMainPageController(ExchangeRateClassService exchangeRate, RateService rateService,
                                  ClientService clientService,
                                  MailSender mailSender) {
        this.exchangeRate = exchangeRate;
        this.rateService = rateService;
        this.clientService = clientService;
        this.mailSender = mailSender;
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

    @GetMapping("/registration")
    public String getSelfRegistration(){
       return "personal/self-registration-page";
    }

    @PostMapping("/registration")
    public String postSelfRegistration(@RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String phoneNumber,
                                       @RequestParam String email,
                                       @RequestParam String pinCode){
        clientService.selfRegistration(firstName, lastName, phoneNumber, email, pinCode);
        return "redirect:/main/personal-area";
    }

    @GetMapping("/login")
    public String login(){
        return "personal/login";
    }

    @GetMapping("/forgot")
    public String getRestorePass(){
        return "/personal/forgot";
    }

    @PostMapping("/forgot")
    public String postRestorePass(@RequestParam String email, @RequestParam String phoneNumber){
        mailSender.restorePassword(email, phoneNumber);
        return "redirect:/restore";
    }

    @GetMapping("/restore")
    public String getRestorePage(){
        return "/personal/restore";
    }

    @PostMapping("/restore")
    public String postRestorePage(@RequestParam String codeFromEmail,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword){
        mailSender.resetPassword(codeFromEmail, phoneNumber, password, confirmPassword);
        return "redirect:/login";
    }
}
