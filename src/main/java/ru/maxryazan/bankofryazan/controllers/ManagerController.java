package ru.maxryazan.bankofryazan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.service.*;

import java.text.ParseException;


@Controller
public class ManagerController {

    private final ClientService clientService;
    private final CreditService creditService;
    private final ServiceClass serviceClass;


    @Autowired
    public ManagerController(ClientService clientService, CreditService creditService, ServiceClass serviceClass) {
        this.clientService = clientService;
        this.creditService = creditService;
        this.serviceClass = serviceClass;

    }

    @GetMapping("/manager")
    public String showManagerPage() {
        return "manager/manager-main-page";
    }

    @GetMapping("/manager/new-client")
    public String getAddNewClient(@ModelAttribute String error) {
        return "manager/new-client";
    }

    @PostMapping("/manager/new-client")
    public String postAddNewClient(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam String phoneNumber,
                                   @RequestParam String email,
                                   @RequestParam String pinCode,
                                   Model model) {
        if (!clientService.validationPhoneNumber(phoneNumber)) {
            return serviceClass.showErrorMessage("Номер телефона не верный!", "manager/new-client", model);
        }
        clientService.save(firstName, lastName, phoneNumber, email, pinCode);
        return "redirect:/manager";
    }

    @GetMapping("manager/credit")
    public String getNewCredit(@ModelAttribute String error) {
        return "manager/new-credit";
    }

    @PostMapping("/manager/credit")
    public String postNewCredit(@RequestParam String phoneNumber,
                                @RequestParam int sumOfCredit,
                                @RequestParam double percentOfCredit,
                                @RequestParam int numberOfPays,
                                Model model) {
        if (!clientService.validationPhoneNumber(phoneNumber)) {
            return serviceClass.showErrorMessage("Номер телефона не верный!", "manager/credit", model);
        }
        if (sumOfCredit <= 9999 || percentOfCredit <= 2 || numberOfPays <= 1) {
            return serviceClass.showErrorMessage("Кредитные данные не корректны!\n" +
                    "Минимальная сумма кредита 10 000 ₽ под 2% годовых. Минимум 2 платежа.", "manager/credit", model);
        }
        Client client = clientService.findByPhoneNumber(phoneNumber);
        Credit credit = creditService.addNewCredit(client, sumOfCredit, percentOfCredit, numberOfPays);
        creditService.save(credit);
        return "redirect:/manager/credit";
    }


    @GetMapping("/manager/contribution")
    public String getNewContribution(@ModelAttribute String error) {
        return "manager/new-contribution";
    }

    @PostMapping("/manager/contribution")
    public String postNewContribution(@RequestParam String phoneNumber,
                                      @RequestParam int sum,
                                      @RequestParam double percent,
                                      @RequestParam int duration,
                                      Model model) {
        if (!clientService.validationPhoneNumber(phoneNumber)) {
            return serviceClass.showErrorMessage("Номер телефона не верный!", "manager/new-contribution", model);
        }
        if (clientService.ifSumNotValid(phoneNumber, sum)) {
            return serviceClass.showErrorMessage("Недостаточно денег для вклада!", "manager/new-contribution", model);
        }
        try {
            clientService.addNewContribution(phoneNumber, sum, percent, duration);
            return "redirect:/manager/contribution";
        } catch (ParseException e) {
            return serviceClass.showErrorMessage("Ошибка парсинга даты!", "/main", model);
        }
    }
}
