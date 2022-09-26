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

    public boolean ifCreditNotExistById(long id) {
        return !creditRepository.existsById(id);
    }

    public void save(Credit credit) {
        creditRepository.save(credit);
    }

    public Credit addNewCredit(Client borrower, int sum, double percent, int numberOfPays) {
        String generatedNumberOfCredit = createValidRandomNumber();

        return new Credit(
                generatedNumberOfCredit,
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

    public String createValidRandomNumber(){
        String generatedNumber;
        do {
            generatedNumber = serviceClass.generateRandomUniqueNumber();
        }  while (ifCreditNotExistByNumberContract(generatedNumber));
        return generatedNumber;
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


    public double creditCalculator(double sumOfCredit, double duration) {
        if(sumOfCredit < 300000){
            return createCreditCalculatorResult(sumOfCredit, duration, 12);
        }
        if(sumOfCredit >= 300000 && sumOfCredit < 600000){
            return createCreditCalculatorResult(sumOfCredit, duration, 10);
        }
        if(sumOfCredit >= 600000 && sumOfCredit < 1200000){
            return createCreditCalculatorResult(sumOfCredit, duration, 8);
        }
        if(sumOfCredit >= 1200000 && sumOfCredit < 5000000){
            return createCreditCalculatorResult(sumOfCredit, duration, 7);
        }
        return createCreditCalculatorResult(sumOfCredit, duration, 5);
    }

    public double createCreditCalculatorResult(double sumOfCredit, double duration, double percent){
        return sumOfCredit + duration * (sumOfCredit *  (percent / 100));
    }

}
