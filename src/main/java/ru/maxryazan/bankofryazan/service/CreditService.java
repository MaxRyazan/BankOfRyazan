package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.CreditRepository;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final ClientService clientService;

    public CreditService(CreditRepository creditRepository, ClientService clientService) {
        this.creditRepository = creditRepository;
        this.clientService = clientService;
    }


    public Credit findById(long id){
        Optional<Credit> credit = creditRepository.findById(id);
        if(credit.isPresent()){
            return credit.get();
        }
        throw  new IllegalArgumentException();
    }

    public void addNewCredit(String phoneNumber, int sum, double percent, int numberOfPays) {
        if (phoneNumber.isBlank() || sum <= 9999 || percent <= 5 || numberOfPays <= 1) {
            throw new IllegalArgumentException("Can borrow minimum 10 000, and minimum 2 pays");
        }


        Client borrower = clientService.findByPhoneNumber(phoneNumber);

        Set<Credit> thisBorrowerCredits = borrower.getCredits();

        Credit credit = new Credit();
        credit.setNumberOfCreditContract(generateRandomUniqueNumber());
        credit.setSumOfCredit(sum);
        credit.setCreditPercent(percent);
        credit.setDateOfBegin(generateDate());
        credit.setEveryMonthPay(generateEveryMonthPay(sum, percent, numberOfPays));
        credit.setSumWithPercents(generateSumWithPercent(sum, percent));
        credit.setNumberOfPays(numberOfPays);
        credit.setBorrower(borrower);
        credit.setRestOfCredit(credit.getSumWithPercents());
        credit.setStatus(Status.ACTIVE);
        thisBorrowerCredits.add(credit);
        creditRepository.save(credit);

    }

    public String generateRandomUniqueNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        do {
            for (int i = 0; i < 8; i++) {
                sb.append(random.nextInt(10));
            }
        } while (!isUnique(sb.toString()));
        return sb.toString();
    }



    public double generateEveryMonthPay(int sum, double percent, int numberOfPays) {
        long sumInKop = sum * 100L;
        double result = (sumInKop * (percent * 0.01) + sumInKop) / numberOfPays;
        int round = (int)result;
        return  (double)round / 100;
    }

    private double generateSumWithPercent(int sum, double percent) {
        long sumInKop = sum * 100L;
        return (sumInKop + sumInKop * (percent / 100)) / 100;
    }

    public boolean isUnique(String str) {
        List<Credit> allCredits = creditRepository.findAll();
        for (Credit cr : allCredits) {
            if (cr.getNumberOfCreditContract().equals(str)) {
                return false;
            }
        }
        return true;
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
           double temp = credit.getSumWithPercents() - (double)((int)(sumOfPays * 100) / 100);

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
            return "/credit-pays";
        } else {
            model.addAttribute("pays", pays);
            model.addAttribute("payd", payd);
            model.addAttribute("numberOfPays", pays.size());
        }
        return "/credit-closed";
    }

    public String generateDateWithHours(){
        Date date = new Date();
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public String generateDate(){
        Date date = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
}
