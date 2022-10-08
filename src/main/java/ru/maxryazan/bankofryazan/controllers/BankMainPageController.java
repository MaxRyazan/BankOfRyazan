package ru.maxryazan.bankofryazan.controllers;


import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.service.*;

@Controller
@Log4j2
public class BankMainPageController {
    private final ExchangeRateClassService exchangeRate;
    private final ClientService clientService;
    private final MailSender mailSender;
    private final CreditService creditService;
    private final ServiceClass serviceClass;

    private final BCryptPasswordEncoder passwordEncoder;


    public BankMainPageController(ExchangeRateClassService exchangeRate,
                                  ClientService clientService,
                                  MailSender mailSender, CreditService creditService,
                                  ServiceClass serviceClass,
                                  BCryptPasswordEncoder passwordEncoder) {
        this.exchangeRate = exchangeRate;
        this.clientService = clientService;
        this.mailSender = mailSender;
        this.creditService = creditService;
        this.serviceClass = serviceClass;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String toMainPage(){
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String showMainPage(Model model, @ModelAttribute String result) {
        log.info("Получаем рейтинги с exchangeRate.getRateFromAPI()");
        try {
            ExchangeRateClass exchangeRateClass = exchangeRate.getRateFromAPI();
            model.addAttribute("USD", exchangeRateClass.getCourse_USD());
            model.addAttribute("EUR", exchangeRateClass.getCourse_EUR());
        } catch (Exception e){
            log.error("ошибка получения рейтинга с API exchangeRate.getRateFromAPI()");
           return "redirect:/login";
        }
        log.info("Успешно загрузили главную страницу  @GetMapping(/main) ");
        return "main-page";
    }

    @PostMapping("/calculate")
    public String postMain(@RequestParam double sumOfCredit, @RequestParam double duration, Model model){
        double result = creditService.creditCalculator(sumOfCredit, duration);
        model.addAttribute("result", result);
        return "main-page";
    }

    @GetMapping("/registration")
    public String getSelfRegistration(@ModelAttribute String error){
       return "personal/self-registration-page";
    }

    @PostMapping("/registration")
    public String postSelfRegistration(@RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String phoneNumber,
                                       @RequestParam String email,
                                       @RequestParam String pinCode,
                                       Model model){
            if(clientService.existsByPhoneAndEmail(phoneNumber, email)){
                log.error("в методе BankMainPageController.postSelfRegistration() произошла ошибка. Попробовали зарегистрировать" +
                        "уже существующего клиента с номером телефона " + phoneNumber);
            return serviceClass.showErrorMessage("Клиент с таким номером телефона или почтой уже существует!",
                    "personal/self-registration-page", model);
        }
             clientService.save(firstName, lastName, phoneNumber, email, passwordEncoder.encode(pinCode));
                log.info("зарегистрирован новый клиент с номером телефона " + phoneNumber);
        return "redirect:/main/personal-area";
    }

    @GetMapping("/login")
    public String login(){
        return "personal/login";
    }

    @GetMapping("/forgot")
    public String getRestorePass(@ModelAttribute String error){
        return "/personal/forgot";
    }

    @PostMapping("/forgot")
    public String postRestorePass(@RequestParam String email, @RequestParam String phoneNumber, Model model){
        if(!clientService.existsByPhoneAndEmail(phoneNumber, email)){
            log.error("введены не верные персональные данные в методе BankMainPageController.postRestorePass()" +
                    phoneNumber + " / " + email);
            return serviceClass.showErrorMessage("Комбинация почта - телефон не верна.", "personal/forgot", model);
        }
        mailSender.restorePassword(email, phoneNumber);
             log.info("выслан проверочный код на по номеру телефона " + phoneNumber);
        return "redirect:/restore";
    }

    @GetMapping("/restore")
    public String getRestorePage(@ModelAttribute String error){
        return "/personal/restore";
    }

    @PostMapping("/restore")
    public String postRestorePage(@RequestParam String codeFromEmail,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model){
        if(!clientService.validationPhoneNumber(phoneNumber)){
            log.error("В методе BankMainPageController.postRestorePage() введен некорректный номер телефона " + phoneNumber);
            return serviceClass.showErrorMessage("Указан не верный номер телефона!", "/personal/restore", model);
        }
        if(clientService.ifCodeFromEmailNotValid(phoneNumber, codeFromEmail)){
            log.error("В методе BankMainPageController.postRestorePage() введен некорректный проверочный код " + phoneNumber
                    + " / " + codeFromEmail);
            return serviceClass.showErrorMessage("Проверочный код не верен!", "/personal/restore", model);
        }
        if(!password.equals(confirmPassword)){
            log.error("В методе BankMainPageController.postRestorePage() произошла ошибка ввода паролей");
            return serviceClass.showErrorMessage("Пароли не совпадают!", "/personal/restore", model);
        }
        mailSender.resetPassword(phoneNumber, password);
            log.info("В методе BankMainPageController.postRestorePage() был успешно изменён пароль пользователя с номером" +
                    " телефона " + phoneNumber);
        return "redirect:/login";
    }
}
