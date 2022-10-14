package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.PayService;
import ru.maxryazan.bankofryazan.service.ServiceClass;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CreditController {

    private final CreditService creditService;
    private final PayService payService;
    private final ClientService clientService;
    private final ServiceClass serviceClass;



    public CreditController(CreditService creditService, PayService payService, ClientService clientService, ServiceClass serviceClass) {
        this.creditService = creditService;
        this.payService = payService;
        this.clientService = clientService;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/credits")
    public String showMainCreditsPage(){
        return "credit/credits-main";
    }

    @GetMapping("/main/personal-area/credit/{id}")
    public String showPays(@PathVariable long id, Model model, @ModelAttribute String error) {
        if(creditService.ifCreditNotExistById(id)){

            return serviceClass.showErrorMessage("Кредита с таким ID не существует!", "personal/personal", model);
        }
        Credit credit = creditService.findById(id);
        return creditService.checkRestOfCredit(credit, model);
    }


    @PostMapping("/main/personal-area/credit-pays")
    public String addPay(@RequestParam double sum,
                         @RequestParam String numberOfCreditContract,
                         HttpServletRequest request,
                         Model model){
        Client client = clientService.findByRequest(request);
        clientService.getPersonalPageArea(model, client);
    if(creditService.ifCreditNotExistByNumberContract(numberOfCreditContract)){
        return serviceClass.showErrorMessage("Кредита с таким номером не существует!", "personal/personal", model);
      }
    if(!payService.validateData(sum, numberOfCreditContract, request)){
        return serviceClass.showErrorMessage("Невозможно осуществить платёж", "personal/personal", model);
      }
    return payService.addNewPay(sum, numberOfCreditContract, client);
     }
}
