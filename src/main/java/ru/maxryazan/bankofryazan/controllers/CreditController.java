package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.CreditService;
import ru.maxryazan.bankofryazan.service.PayService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CreditController {

    private final CreditService creditService;
    private final PayService payService;
    private final ClientService clientService;



    public CreditController(CreditService creditService, PayService payService, ClientService clientService) {
        this.creditService = creditService;
        this.payService = payService;

        this.clientService = clientService;
    }

    @GetMapping("/credit/{id}")
    public String showPays(@PathVariable long id, Model model) {

        Credit credit = creditService.findById(id);
        List<Pay> pays = credit.getPays();
        double payd = 0;
        for (Pay pp : credit.getPays()) {
            payd += pp.getSum();
        }
        if(Math.round(credit.getRestOfCredit()) == 0){
             credit.setStatus(Status.CLOSED);
             creditService.save(credit);
        }
        if(credit.getStatus().equals(Status.ACTIVE)) {
        model.addAttribute("everyMonthPay", credit.getEveryMonthPay());
        model.addAttribute("pays", pays);
        model.addAttribute("payd", payd);
        model.addAttribute("ost", (double) Math.round(credit.getSumWithPercents() - payd));
        model.addAttribute("numberOfPays", pays.size());
        model.addAttribute("ostPays", credit.getNumberOfPays() - pays.size());
        model.addAttribute("creditNumber", credit.getNumberOfCreditContract());
        return "/credit-pays";
        } else {
            model.addAttribute("pays", pays);
            model.addAttribute("payd", payd);
            model.addAttribute("numberOfPays", pays.size());
    }
        return "/credit-closed";
    }


    @PostMapping("/credit-pays")
    public String addPay(@RequestParam double sum, @RequestParam String numberOfCreditContract, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String authClientPhoneNumber = principal.getName();
        Client client = clientService.findByPhoneNumber(authClientPhoneNumber);

        Date date = new Date();
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Credit credit =  creditService.findByNumberOfCreditContract(numberOfCreditContract);
        if(credit.getStatus().equals(Status.ACTIVE)) {
            if (client.getBalance() >= sum && sum > 0) {
                if (credit.getRestOfCredit() >= sum) {
                    Pay pay = new Pay(
                            simpleDateFormat.format(date),
                            sum,
                            credit
                    );
                    payService.save(pay);
                    client.setBalance(client.getBalance() - pay.getSum());
                    if(Math.round(credit.getRestOfCredit()) == 0) {
                        credit.setStatus(Status.CLOSED);
                    }
                    creditService.saveOrUpdateCredit(credit);
                    return "redirect:/main/personal-area";
                }
            }
            throw new IllegalArgumentException("Недостаточно средств!");
        }
        throw new IllegalArgumentException("Кредит закрыт!");
    }
}
