package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Contribution;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ContributionService;
import ru.maxryazan.bankofryazan.service.TransactionalService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonalAreaController {
    private final ClientService clientService;
    private final ContributionService contributionService;

    private final TransactionalService transactionalService;

    public PersonalAreaController(ClientService clientService, ContributionService contributionService, TransactionalService transactionalService) {
        this.clientService = clientService;
        this.contributionService = contributionService;
        this.transactionalService = transactionalService;
    }


    @GetMapping("/main/personal-area")
    public String openPersonalArea(Model model, HttpServletRequest request) {
        Client client = clientService.findByRequest(request);
        contributionService.checkEndDateOfContributions(client.getContributions());

        model.addAttribute("firstName", client.getFirstName());
        model.addAttribute("lastName", client.getLastName());
        model.addAttribute("phone", client.getPhoneNumber());
        model.addAttribute("balance", (double)((int)(client.getBalance() * 100)) / 100);
        model.addAttribute("incoming", client.getInComingTransactions());
        model.addAttribute("outcoming", client.getOutComingTransactions());
        model.addAttribute("credits", client.getCredits());
        model.addAttribute("investments", client.getInvestments());
        for(Contribution cn : client.getContributions()) {
            if (cn.getStatus().equals(Status.ACTIVE)) {
                model.addAttribute("contributions", client.getContributions());
            }
        }
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
