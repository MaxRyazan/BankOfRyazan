package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.InvestmentService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final ClientService clientService;

    private final ServiceClass serviceClass;


    public InvestmentController(InvestmentService investmentService, ClientService clientService, ServiceClass serviceClass) {
        this.investmentService = investmentService;
        this.clientService = clientService;
        this.serviceClass = serviceClass;
    }


    @GetMapping("/main")
    public String getMainInvestments(Model model, @ModelAttribute String error) {
     return investmentService.createMainPage(model);
    }

    @PostMapping("/main")
    public String postMainInvestments(@RequestParam String type,
                                      @RequestParam String amount,
                                      HttpServletRequest request,
                                      Model model) {
        Client client = clientService.findByRequest(request);
    try {
         if (client.getBalance() < investmentService.calculatePriceOfInvestment(investmentService.changeType(type), amount)) {
             investmentService.createMainPage(model);
             return serviceClass.showErrorMessage("Недостаточно денег!", "investments/investments-main", model);
         }
        investmentService.createInvestment(type, amount, client);
        clientService.save(client);
    } catch (Exception e){
         return serviceClass.showErrorMessage("Ошибка получения данных по текущей дате!!",
                 "investments/investments-main", model);
    }
            return "redirect:/investments/main";
        }

}
