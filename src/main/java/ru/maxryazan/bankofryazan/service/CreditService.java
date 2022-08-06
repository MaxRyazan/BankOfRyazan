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
        Optional<Credit> credit = creditRepository.findById(id);
        if(credit.isPresent()){
            return credit.get();
        }
        throw  new IllegalArgumentException();
    }

    public void addNewCredit(String phoneNumber, int sum, double percent, int numberOfPays) {
        if (phoneNumber.isBlank() || sum <= 9999 || percent <= 2 || numberOfPays <= 1) {
            throw new IllegalArgumentException("Can borrow minimum 10 000, min 2%,  and minimum 2 pays");
        }
        Client borrower = clientService.findByPhoneNumber(phoneNumber);

        List<Credit> thisBorrowerCredits = borrower.getCredits();

        Credit credit = new Credit();
        String num;
        do {
            Random random = new Random();
            num = serviceClass.generateRandomUniqueNumber(random);
        }  while (!isUnique(num));
        credit.setNumberOfCreditContract(num);
        credit.setSumOfCredit(sum);
        credit.setCreditPercent(percent);
        credit.setDateOfBegin(serviceClass.generateDate());
        credit.setEveryMonthPay(serviceClass.generateEveryMonthPay(sum, percent, numberOfPays));
        credit.setSumWithPercents(serviceClass.generateSumWithPercent(sum, percent));
        credit.setNumberOfPays(numberOfPays);
        credit.setBorrower(borrower);
        credit.setRestOfCredit(credit.getSumWithPercents());
        credit.setStatus(Status.ACTIVE);
        thisBorrowerCredits.add(credit);
        creditRepository.save(credit);

    }


    public void save(Credit credit) {
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
           double temp = credit.getSumWithPercents() - serviceClass.roundToDoubleWithTwoSymbolsAfterDot(sumOfPays);

                credit.setRestOfCredit(temp);
                save(credit);
            }
        }
    }


    public Credit findByNumberOfCreditContract(String numberOfCreditContract) {
      Optional<Credit> cr  = creditRepository.findAll().stream().filter(
              credit -> credit.getNumberOfCreditContract().equals(numberOfCreditContract)).findFirst();
        if(cr.isPresent()){
            return cr.get();
        }
        throw  new IllegalArgumentException();
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
       Credit credit = creditRepository.findAll().stream()
               .filter(isUniqueCredit -> isUniqueCredit.getNumberOfCreditContract().equals(str)).findFirst().orElse(null);
             return credit == null;
    }

}
