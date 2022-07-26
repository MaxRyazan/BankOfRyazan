package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.TransactionalService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonalAreaController {
    private final ClientService clientService;

    private final TransactionalService transactionalService;

    public PersonalAreaController(ClientService clientService, TransactionalService transactionalService) {
        this.clientService = clientService;
        this.transactionalService = transactionalService;
    }


    @GetMapping("/main/personal-area")
    public String openPersonalArea(Model model, HttpServletRequest request) {
      return  clientService.getPersonalPageArea(model, request);

    }


    @PostMapping("/main/personal-area")
    public String postTransaction(@RequestParam String recipientPhoneNumber, int sum, HttpServletRequest request) {
        if(sum >= 1) {
            transactionalService.createNewTransaction(clientService.validationPhoneNumber(recipientPhoneNumber), sum, request);
        }
        return "redirect:/main/personal-area";
    }


}
