package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
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


    public void addNewCredit(String phoneNumber, int sum, double percent, int numberOfPays) {
        if(phoneNumber.isBlank() || sum <= 9999 || percent <= 5 || numberOfPays <= 1){
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
        credit.setNumberOfPays(numberOfPays);
        credit.setBorrower(borrower);

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
        return (((sumInKop +  sumInKop * (percent / 100)) / numberOfPays) / 100);
    }

    private boolean isUnique(String str) {
    List<Credit> allCredits = creditRepository.findAll();
        for(Credit cr : allCredits) {
            if(cr.getNumberOfCreditContract().equals(str)) {
                return false;
            }
        }
        return true;
    }
}
