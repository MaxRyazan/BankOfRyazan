package ru.maxryazan.bankofryazan.controllers;

import org.springframework.mail.javamail.JavaMailSender;
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
    private final EmailCodeSenderService emailCodeSenderService;
    private final MailSender mailSender;

    public BankMainPageController(ExchangeRateClassService exchangeRate, RateService rateService,
                                  ClientService clientService, EmailCodeSenderService emailCodeSenderService,
                                  MailSender mailSender) {
        this.exchangeRate = exchangeRate;
        this.rateService = rateService;
        this.clientService = clientService;
        this.emailCodeSenderService = emailCodeSenderService;
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
        Client client = clientService.findByPhoneNumber(phoneNumber);

        if(client.getEmail().equals(email.replace(" ", ""))){
            String subject = "Password restore";
            String code = emailCodeSenderService.generateCode(client.getLastName());
            String message = "Your code for password restore : " + code;
            mailSender.send(email, subject, message);

            EmailCodeSender codeSender = new EmailCodeSender();
            codeSender.setValue(code);
            codeSender.setPassRestorer(client);
            client.getEmailCodes().add(codeSender);
            emailCodeSenderService.save(codeSender);
        }
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
        Client client = clientService.findByPhoneNumber(phoneNumber);
        String codeFromDB = emailCodeSenderService.findByPhoneNumber(phoneNumber);
        if(codeFromEmail.equals(codeFromDB) && password.equals(confirmPassword)){
            client.setPinCode(clientService.passwordEncoder().encode(password));
            clientService.save(client);
            emailCodeSenderService.deleteAll(client);
        } else {
           throw  new IllegalArgumentException();
        }
        return "redirect:/login";
    }
}
