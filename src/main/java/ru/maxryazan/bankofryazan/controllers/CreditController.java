package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.PayService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CreditController {

    private final CreditService creditService;
    private final PayService payService;



    public CreditController(CreditService creditService, PayService payService) {
        this.creditService = creditService;
        this.payService = payService;
    }

    @GetMapping("/credit/{id}")
    public String showPays(@PathVariable long id, Model model) {
        Credit credit = creditService.findById(id);
        return creditService.checkRestOfCredit(credit, model);
    }


    @PostMapping("/credit-pays")
    public String addPay(@RequestParam double sum, @RequestParam String numberOfCreditContract,
                         HttpServletRequest request){
    return payService.addNewPay(sum, numberOfCreditContract, request);
    }
}
