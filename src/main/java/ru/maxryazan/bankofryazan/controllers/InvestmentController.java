package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.InvestmentService;
import javax.servlet.http.HttpServletRequest;



@Controller
@RequestMapping("/investments")
public class InvestmentController {

    private final InvestmentService investmentService;
    private final ClientService clientService;


    public InvestmentController(InvestmentService investmentService, ClientService clientService) {
        this.investmentService = investmentService;
        this.clientService = clientService;
    }


    @GetMapping("/main")
    public String getMainInvestments(Model model, HttpServletRequest request) {
     request.getSession();
     return investmentService.createMainPage(model);
    }

    @PostMapping("/main")
    public String postMainInvestments(@RequestParam String type,
                                      @RequestParam String amount,
                                      HttpServletRequest request) {
        Client client = clientService.findByRequest(request);
        investmentService.createInvestment(type, amount, client);
        clientService.save(client);
            return "redirect:/investments/main";
        }



    @GetMapping("/make")
    public String getMakeInvestment(){
        return "/investments/investments-make";
    }

}
