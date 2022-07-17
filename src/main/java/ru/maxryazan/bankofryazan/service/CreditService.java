package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.CreditRepository;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public record CreditService(CreditRepository creditRepository, ClientService clientService) {

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
        Date date = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Client borrower = clientService.findByPhoneNumber(phoneNumber);

        Set<Credit> thisBorrowerCredits = borrower.getCredits();

        Credit credit = new Credit();
        credit.setNumberOfCreditContract(generateCreditNumber());
        credit.setSumOfCredit(sum);
        credit.setCreditPercent(percent);
        credit.setDateOfBegin(simpleDateFormat.format(date));
        credit.setEveryMonthPay(generateEveryMonthPay(sum, percent, numberOfPays));
        credit.setSumWithPercents(generateSumWithPercent(sum, percent));
        credit.setNumberOfPays(numberOfPays);
        credit.setBorrower(borrower);
        credit.setRestOfCredit(credit.getSumWithPercents());
        credit.setStatus(Status.ACTIVE);
        thisBorrowerCredits.add(credit);
        creditRepository.save(credit);

    }

    private String generateCreditNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        do {
            for (int i = 0; i < 8; i++) {
                sb.append(random.nextInt(10));
            }
        } while (!isUnique(sb.toString()));
        return sb.toString();
    }

    private double generateEveryMonthPay(int sum, double percent, int numberOfPays) {
        long sumInKop = sum * 100L;
        double result = (sumInKop * (percent * 0.01) + sumInKop) / numberOfPays;
        int round = (int)result;
        return  (double)round / 100;
    }

    private double generateSumWithPercent(int sum, double percent) {
        long sumInKop = sum * 100L;
        return (sumInKop + sumInKop * (percent / 100)) / 100;
    }

    private boolean isUnique(String str) {
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
}
