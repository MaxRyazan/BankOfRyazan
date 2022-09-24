package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.PayService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CreditController {

    private final CreditService creditService;
    private final PayService payService;

    private final ServiceClass serviceClass;



    public CreditController(CreditService creditService, PayService payService, ServiceClass serviceClass) {
        this.creditService = creditService;
        this.payService = payService;
        this.serviceClass = serviceClass;
    }

    @GetMapping("/credit/{id}")
    public String showPays(@PathVariable long id, Model model, @ModelAttribute String error) {
        if(creditService.ifCreditNotExistById(id)){
            return serviceClass.showErrorMessage("Кредита с таким ID не существует!", "main/personal-area", model);
        }
        Credit credit = creditService.findById(id);
        return creditService.checkRestOfCredit(credit, model);
    }


    @PostMapping("/credit-pays")
    public String addPay(@RequestParam double sum, @RequestParam String numberOfCreditContract,
                         HttpServletRequest request, Model model){
    if(creditService.ifCreditNotExistByNumberContract(numberOfCreditContract)){
        return serviceClass.showErrorMessage("Кредита с таким номером не существует!", "main/personal-area", model);
        }
    return payService.addNewPay(sum, numberOfCreditContract, request);
    }
}
