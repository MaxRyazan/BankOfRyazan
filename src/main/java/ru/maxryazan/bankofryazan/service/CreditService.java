package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.*;
import ru.maxryazan.bankofryazan.repository.CreditRepository;

@Service
public class CreditService {

    private final CreditRepository creditRepository;
    private final ServiceClass serviceClass;

    public CreditService(CreditRepository creditRepository, ServiceClass serviceClass) {
        this.creditRepository = creditRepository;
        this.serviceClass = serviceClass;
    }

    public Credit findByNumberOfCreditContract(String numberOfCreditContract) {
        return creditRepository.findByNumberOfCreditContract(numberOfCreditContract);
    }

    public Credit findById(long id){
        return creditRepository.findById(id);
    }

    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    public Credit addNewCredit(Client borrower, int sum, double percent, int numberOfPays) {
        String num = createAnValidateRandomNumber();

        return new Credit(
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
    }

    public boolean ifCreditNotExistByNumberContract(String number) {
        return !creditRepository.existsByNumberOfCreditContract(number);
    }

    public String createAnValidateRandomNumber(){
        String num;
        do {
            num = serviceClass.generateRandomUniqueNumber();
        }  while (ifCreditNotExistByNumberContract(num));
        return num;
    }


    public void setRestOfCreditOrCloseStatus(Credit credit) {
            double sumOfPays = credit.getPays().stream().mapToDouble(Pay::getSum).sum();
            credit.setRestOfCredit(credit.getSumWithPercents() - sumOfPays);
            if (credit.getRestOfCredit() < 1) {
                credit.setRestOfCredit(0);
                credit.setStatus(Status.CLOSED);
            }
            save(credit);
    }


    public String checkRestOfCredit(Credit credit, Model model) {
        setRestOfCreditOrCloseStatus(credit);
        model.addAttribute(credit);
        if(credit.getStatus().equals(Status.ACTIVE)) {
            return "credit/credit-pays";
        } else {
            return "credit/credit-closed";
        }
    }


    public double creditCalculator(double sumOfCredit, int duration) {
        double result = 0;
        if(sumOfCredit < 300000){
            result = sumOfCredit + duration * (sumOfCredit *  0.12);
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
        if(sumOfCredit >= 5000000 && sumOfCredit <= 10000000){
            result =  sumOfCredit + duration * (sumOfCredit *  0.05);
        }
        return (double)((int)(result * 100)) / 100;
    }

    public boolean ifCreditNotExistById(long id) {
        return !creditRepository.existsById(id);
    }
}
