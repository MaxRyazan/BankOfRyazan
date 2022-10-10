package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Pay;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.PayRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    private PayService payService;
    @Mock
    PayRepository payRepository;
    @Mock
    CreditService creditService;
    @Mock
    ClientService clientService;
    @Mock
    ServiceClass serviceClass;

    @BeforeEach
    public void setup(){
        payService = new PayService(payRepository, creditService,
                clientService, serviceClass);
    }


    @Test
    void addNewPay() {
        double sum = 5000d;
        String numberOfCreditContract = "1234567";
        Client client = new Client();

       assertEquals("personal/personal",
               payService.addNewPay(sum, numberOfCreditContract, client));

    }

    @Test
    void createPay() {
    }

    @Test
    void validateData() {
    }
}