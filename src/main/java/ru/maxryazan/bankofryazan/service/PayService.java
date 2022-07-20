package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.PayRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class PayService {

    private final PayRepository payRepository;
    private final CreditService creditService;
    private final ClientService clientService;

    public PayService(PayRepository payRepository, CreditService creditService, ClientService clientService) {
        this.payRepository = payRepository;
        this.creditService = creditService;
        this.clientService = clientService;
    }


    public void save(Pay pay){
        payRepository.save(pay);
    }

    public String addNewPay(double sum, String numberOfCreditContract, HttpServletRequest request){

        Client client = clientService.findByRequest(request);

        Credit credit =  creditService.findByNumberOfCreditContract(numberOfCreditContract);

        if(credit.getStatus().equals(Status.ACTIVE)) {
            if (client.getBalance() >= sum && sum > 0) {
                if (credit.getRestOfCredit() >= sum) {
                    Pay pay = new Pay(
                            creditService.generateDateWithHours(),
                            sum,
                            credit
                    );
                    save(pay);
                    client.setBalance(client.getBalance() - pay.getSum());
                    if(Math.round(credit.getRestOfCredit()) == 0) {
                        credit.setStatus(Status.CLOSED);
                    }
                    creditService.saveOrUpdateCredit(credit);
                    return "redirect:/main/personal-area";
                }
            }
            throw new IllegalArgumentException("Кредит закрыт!");
        }
        throw new IllegalArgumentException("addNewPay error");
    }


}
