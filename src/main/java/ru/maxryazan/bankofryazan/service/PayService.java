package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.PayRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Service
public class PayService {

    private final PayRepository payRepository;
    private final CreditService creditService;
    private final ClientService clientService;

    private final ServiceClass serviceClass;

    public PayService(PayRepository payRepository, CreditService creditService, ClientService clientService, ServiceClass serviceClass) {
        this.payRepository = payRepository;
        this.creditService = creditService;
        this.clientService = clientService;
        this.serviceClass = serviceClass;
    }


    public void save(Pay pay){
        payRepository.save(pay);
    }

    public String addNewPay(double sum, String numberOfCreditContract, HttpServletRequest request) {

        Client client = clientService.findByRequest(request);
        Credit credit = creditService.findByNumberOfCreditContract(numberOfCreditContract);

        Date date = new Date();
        Pay pay = new Pay(
                serviceClass.generateDateWithHours(date),
                sum,
                credit
        );
        save(pay);
        client.setBalance(client.getBalance() - pay.getSum());
        if (Math.round(credit.getRestOfCredit()) == 0) {
            credit.setStatus(Status.CLOSED);
        }
        creditService.saveOrUpdateCredit(credit);
        return "redirect:/main/personal-area";
    }

    public boolean validateData(double sum, String numberOfCreditContract, HttpServletRequest request) {
        Credit credit = creditService.findByNumberOfCreditContract(numberOfCreditContract);
        Client client = clientService.findByRequest(request);
        return credit.getStatus().equals(Status.ACTIVE)
               && sum > 0
               && client.getBalance() >= sum;
    }

}
