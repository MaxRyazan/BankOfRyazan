package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Investment;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.InvestmentService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


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
    public String getMainInvestments(Model model, HttpServletRequest request) {
     request.getSession();
     return investmentService.createMainPage(model);
    }

    @PostMapping("/main")
    public String postMainInvestments(@RequestParam String type,
                                      @RequestParam String amount,
                                      HttpServletRequest request) {
        Client client = clientService.findByRequest(request);
        if(client.getBalance() > investmentService.setPriceOfInvestment(investmentService.changeType(type), amount)) {
            Investment investment = new Investment();
            String typeOfInvestment = investmentService.changeType(type);
            investment.setInvestor(client);
            investment.setDateOfInvestment(serviceClass.generateDate());
            investment.setType(typeOfInvestment);
            investment.setInvestmentSizeByUnits(Double.parseDouble(amount));
            investment.setBasePriceOfInvestment(investmentService.setPriceOfInvestment(typeOfInvestment, amount));
            investment.setCurrPriceOfInvestment(investmentService.setPriceOfInvestment(typeOfInvestment, amount));
            investmentService.save(investment);
            client.getInvestments().add(investment);
            client.setBalance(client.getBalance() - investment.getBasePriceOfInvestment());
            clientService.save(client);
            return "redirect:/investments/main";
        }
       throw  new IllegalArgumentException("not enough money for investment");
    }


    @GetMapping("/make")
    public String getMakeInvestment(){
        return "/investments/investments-make";
    }

    public float createPricePerGram(float pricePerOunce){
        return  ((float)((int)(pricePerOunce / 28.3495 * 100000))) / 100000;
    }
}
