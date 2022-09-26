package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.CreditRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    CreditService creditService;
    @Mock
    CreditRepository creditRepository;
    @Mock
    ServiceClass serviceClass;

    @BeforeEach
    void createCreditService(){
        creditService = new CreditService(creditRepository, serviceClass);
    }

    private double testRound(double number){
        return (double)((int)(number * 100)) / 100;
    }

    @Test
    void setRestOfCreditOrCloseStatus() {

        Credit credit = new Credit();
        Pay pay = new Pay(
                String.valueOf(new Date()),
                3500,
                 credit);

        Pay pay1 = new Pay(
                String.valueOf(new Date()),
                1499.66,
                credit);


        credit.setPays(List.of(pay, pay1));
        credit.setSumWithPercents(5000);
        credit.setStatus(Status.ACTIVE);

        creditService.setRestOfCreditOrCloseStatus(credit);

        assertEquals(Status.CLOSED, credit.getStatus());
        assertEquals(0, credit.getRestOfCredit());
    }

    @Test
    void setRestOfCreditOrCloseStatus2() {

        Credit credit = new Credit();
        Pay pay = new Pay(
                String.valueOf(new Date()),
                2500,
                credit);

        Pay pay1 = new Pay(
                String.valueOf(new Date()),
                1499.66,
                credit);


        credit.setPays(List.of(pay, pay1));
        credit.setSumWithPercents(5000);
        credit.setStatus(Status.ACTIVE);

        creditService.setRestOfCreditOrCloseStatus(credit);

        assertEquals(Status.ACTIVE, credit.getStatus());
        assertEquals(1000.34, testRound(credit.getRestOfCredit()));
    }

    @Test
    void ifCreditNotExistByNumberContract() {
        Credit credit = new Credit();
        String numberOfCredit = "1234567";
        credit.setNumberOfCreditContract("numberOfCredit");

        given(creditRepository.existsByNumberOfCreditContract(numberOfCredit)).willReturn(true);

        assertFalse(creditService.ifCreditNotExistByNumberContract(numberOfCredit));
        assertTrue(creditService.ifCreditNotExistByNumberContract("someString"));
    }

    @Test
    void addNewCredit() {
        Client client = new Client();
        client.setCredits(new ArrayList<>());
        given(serviceClass.generateRandomUniqueNumber()).willReturn("1234567");
        given(creditService.ifCreditNotExistByNumberContract(any())).willReturn( true);
        given(serviceClass.generateDate()).willReturn("10.12.87 20:20");

        Credit credit = creditService.addNewCredit(client, 10000, 10, 12);

        assertEquals(client, credit.getBorrower());
    }


    @Test
    void checkRestOfCredit() {
        Credit credit = new Credit();
        credit.setStatus(Status.ACTIVE);
        credit.setPays(new ArrayList<>());
        credit.setSumWithPercents(10000);
        Model model = Mockito.mock(Model.class);

        String result = creditService.checkRestOfCredit(credit, model);
        assertEquals("credit/credit-pays", result);
    }
    @Test
    void checkRestOfCredit2() {
        Credit credit = new Credit();
        credit.setStatus(Status.CLOSED);
        credit.setPays(new ArrayList<>());
        credit.setSumWithPercents(10000);
        Model model = Mockito.mock(Model.class);

        String result = creditService.checkRestOfCredit(credit, model);
        assertEquals("credit/credit-closed", result);
    }

    @Test
    void creditCalculator() {
        double result = creditService.creditCalculator(100000, 2);
        assertEquals(124000, result);
    }
    @Test
    void creditCalculator2() {
        double result = creditService.creditCalculator(300000, 2);
        assertEquals(360000, result);
    }
    @Test
    void creditCalculator3() {
        double result = creditService.creditCalculator(600000, 2);
        assertEquals(696000, result);
    }
    @Test
    void creditCalculator4() {
        double result = creditService.creditCalculator(1200000, 2);
        assertEquals(1368000, result);
    }
    @Test
    void creditCalculator5() {
        double result = creditService.creditCalculator(10000000, 2);
        assertEquals(11000000, result);
    }


    @Test
    void createAnValidateRandomNumber() {

        given(serviceClass.generateRandomUniqueNumber()).willReturn("1234567");
        given(creditService.ifCreditNotExistByNumberContract("1234567")).willReturn(true);

        String num = creditService.createValidRandomNumber();

        assertEquals(7, num.length());
    }

    @Test
    void createCreditCalculatorResult() {
        double result = creditService.createCreditCalculatorResult(100000, 0.5, 12);
        assertEquals(106000, result);
    }
    @Test
    void createCreditCalculatorResult2() {
        double result = creditService.createCreditCalculatorResult(600000, 2, 8);
        assertEquals(696000, result);
    }
}