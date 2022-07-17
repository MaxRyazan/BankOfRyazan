package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.PayService;
import ru.maxryazan.bankofryazan.service.TransactionalService;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Principal principal = request.getUserPrincipal();
        String authClientPhoneNumber = principal.getName();
        Client client = clientService.findByPhoneNumber(authClientPhoneNumber);
        model.addAttribute("firstName", client.getFirstName());
        model.addAttribute("lastName", client.getLastName());
        model.addAttribute("phone", client.getPhoneNumber());
        model.addAttribute("balance", (double)((int)(client.getBalance()*100))/100);
        model.addAttribute("incoming", client.getInComingTransactions());
        model.addAttribute("outcoming", client.getOutComingTransactions());
        model.addAttribute("credits", client.getCredits());
        model.addAttribute("investments", client.getInvestments());
        model.addAttribute("contributions", client.getContributions());
        return "personal_area-page";
    }




    @PostMapping("/main/personal-area")
    public String postTransaction(@RequestParam String recipientPhoneNumber, int sum, HttpServletRequest request) {
        if(!recipientPhoneNumber.isBlank() && sum >= 1) {
            transactionalService.createNewTransaction(recipientPhoneNumber, sum, request);
        }
        return "redirect:/main/personal-area";
    }


}
