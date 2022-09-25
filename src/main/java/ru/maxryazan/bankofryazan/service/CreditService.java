package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.*;
import ru.maxryazan.bankofryazan.repository.CreditRepository;
import java.util.*;


@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final ClientService clientService;
    private final ServiceClass serviceClass;

    public CreditService(CreditRepository creditRepository, ClientService clientService, ServiceClass serviceClass) {
        this.creditRepository = creditRepository;
        this.clientService = clientService;
        this.serviceClass = serviceClass;
    }


    public Credit findById(long id){
        return creditRepository.findById(id);
    }

    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    public void addNewCredit(String phoneNumber, int sum, double percent, int numberOfPays) {
        Client borrower = clientService.findByPhoneNumber(phoneNumber);
        String num;
        do {
            num = serviceClass.generateRandomUniqueNumber();
        }  while (!isUnique(num));
        Credit credit = new Credit(
                num,
                sum,
                percent,
                serviceClass.generateDate(),
                serviceClass.generateEveryMonthPay(sum, percent, numberOfPays),
                serviceClass.generateSumWithPercent(sum, percent),
                numberOfPays,
                borrower,
                serviceClass.generateSumWithPercent(sum, percent),
                Status.ACTIVE
        );
        creditRepository.save(credit);
    }


    public void saveOrUpdateCredit(Credit credit) {
        if (credit.getPays() != null) {
            if (credit.getPays().size() == 0) {
                credit.setRestOfCredit(credit.getSumWithPercents());
                save(credit);
            } else {
                double sumOfPays = 0;
           for(Pay pp : credit.getPays()) {
               sumOfPays += pp.getSum();
           }
           double temp = credit.getSumWithPercents() - sumOfPays;
                credit.setRestOfCredit(temp);
                save(credit);
            }
        }
    }


    public Credit findByNumberOfCreditContract(String numberOfCreditContract) {
        return creditRepository.findByNumberOfCreditContract(numberOfCreditContract);
    }


    public String checkRestOfCredit(Credit credit, Model model) {
        List<Pay> pays = credit.getPays();
        double payd = 0;
        for (Pay pp : credit.getPays()) {
            payd += pp.getSum();
        }
        if(Math.round(credit.getRestOfCredit()) == 0){
            credit.setStatus(Status.CLOSED);
            save(credit);
        }

        if(credit.getStatus().equals(Status.ACTIVE)) {
            model.addAttribute("everyMonthPay", credit.getEveryMonthPay());
            model.addAttribute("pays", pays);
            model.addAttribute("payd", payd);
            model.addAttribute("ost", (double) Math.round(credit.getSumWithPercents() - payd));
            model.addAttribute("numberOfPays", pays.size());
            model.addAttribute("ostPays", credit.getNumberOfPays() - pays.size());
            model.addAttribute("creditNumber", credit.getNumberOfCreditContract());
            return "credit/credit-pays";
        } else {
            model.addAttribute("pays", pays);
            model.addAttribute("payd", payd);
            model.addAttribute("numberOfPays", pays.size());
        }
        return "credit/credit-closed";
    }


    private boolean isUnique(String str) {
     return !creditRepository.existsByNumberOfCreditContract(str);
    }

    public double creditCalculator(double sumOfCredit, int duration) {
        double result = 0;
        if(sumOfCredit < 300000){
            result = sumOfCredit * duration * (sumOfCredit *  0.12);
        }
        if(sumOfCredit >= 300000 && sumOfCredit < 600000){
            result =  sumOfCredit + duration * (sumOfCredit * 0.1);
        }
        if(sumOfCredit >= 600000 && sumOfCredit < 1200000){
            result =  sumOfCredit + duration * (sumOfCredit *  0.08);
        }
        if(sumOfCredit >= 1200000 && sumOfCredit < 5000000){
            result =  sumOfCredit + duration * (sumOfCredit *  0.07);
        }
        if(sumOfCredit >= 5000000 && sumOfCredit < 10000000){
            result =  sumOfCredit + duration * (sumOfCredit *  0.05);
        }
        return (double)((int)(result * 100)) / 100;
    }

    public boolean ifCreditNotExistByNumberContract(String number) {
        return !creditRepository.existsByNumberOfCreditContract(number);
    }

    public boolean ifCreditNotExistById(long id) {
        return !creditRepository.existsById(id);
    }
}
