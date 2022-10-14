package ru.maxryazan.bankofryazan.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.service.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Log4j2
@Controller
public class PersonalAreaController {
    private final ClientService clientService;
    private final TransactionalService transactionalService;
    private final CarInsuranceService carInsuranceService;
    private final ServiceClass serviceClass;
    private final SettingsService settingsService;
    private final MailSender mailSender;

    public PersonalAreaController(ClientService clientService,
                                  TransactionalService transactionalService,
                                  CarInsuranceService carInsuranceService, ServiceClass serviceClass, SettingsService settingsService, MailSender mailSender) {
        this.clientService = clientService;
        this.transactionalService = transactionalService;
        this.carInsuranceService = carInsuranceService;
        this.serviceClass = serviceClass;
        this.settingsService = settingsService;
        this.mailSender = mailSender;
    }


    @GetMapping("/main/personal-area")
    public String openPersonalArea(Model model, HttpServletRequest request,
                                   @ModelAttribute String error) {
        log.info("Попытка зайти в личный кабинет: " + request.getUserPrincipal().getName());
        Client client = clientService.findByRequest(request);
        log.info("Авторизованный пользователь: " + client);
        try{
            log.info("Проверяем страховки  @GetMapping(/main/personal-area)");
            String insuranceExpired = carInsuranceService.checkInsuranceForExpiration(client);
            model.addAttribute("insuranceExpired", insuranceExpired);
        } catch (Exception e) {
            return serviceClass.showErrorMessage("Ошибка!",
                    "redirect:/main", model);
        }
        log.info("Страховки проверены  @GetMapping(/main/personal-area)");

            if (settingsService.isTransactionalSecure(clientService.findByRequest(request))) {
                mailSender.sendCodeToEmail(client);
        }
        return clientService.getPersonalPageArea(model, client);
    }


    @PostMapping("/main/personal-area")
    public String postTransaction(@RequestParam String recipientPhoneNumber,
                                  int sum, HttpServletRequest request, Model model) {
        if (settingsService.checkDoAllTransactionsWithSecretCode(clientService.findByRequest(request))) {
            return transactionalService.doTransaction(recipientPhoneNumber, sum, request, model);
        } else {
            model.addAttribute("client", clientService.findByRequest(request));
            return serviceClass.showErrorMessage("Установлено ограничение на совершение транзакций!",
                    "personal/personal", model);
        }
    }

    @PostMapping("/main/personal-area/secure-transaction")
    public String postTransaction(@RequestParam String recipientPhoneNumber,
                                  @RequestParam String codeFromClientForm,
                                  int sum, HttpServletRequest request, Model model) {
        Client authClient =  clientService.findByRequest(request);
        model.addAttribute("client", authClient);
        if (settingsService.isCodesEquals(authClient, codeFromClientForm)) {
            return transactionalService.doTransaction(recipientPhoneNumber, sum, request, model);
        } else {
            return serviceClass.showErrorMessage("Проверочный код не верен!",
                    "personal/personal", model);
        }
    }

    @PostMapping("/personal/buy_car_insurance")
    public String postBuyCarInsurance(@RequestParam String carNumber,
                                      @RequestParam int horsePower,
                                      @RequestParam int year,
                                      @RequestParam int drivers,
                                      @RequestParam String isTaxi,
                                      Model model,
                                      HttpServletRequest request) {
        return carInsuranceService.validateAllData(year, carNumber, horsePower, drivers, isTaxi, model, request);
    }
}
