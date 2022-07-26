package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.maxryazan.bankofryazan.models.Credit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class CreditServiceTest {
    ServiceClass serviceClass = Mockito.mock(ServiceClass.class);


    @Test
    void generateRandomUniqueNumberTest() {
//        String pattern = "[0-9]+";
//        Mockito.when(creditService.generateRandomUniqueNumber().length() == 8
//            && creditService.generateRandomUniqueNumber().equals(pattern))
//            .thenReturn(true);

    }

    @Test
    void generateEveryMonthPayTest() {
        Mockito.when(serviceClass.generateEveryMonthPay(10000, 10, 10)).thenReturn(1100d);
        Mockito.when(serviceClass.generateEveryMonthPay(20000, 3.5, 2)).thenReturn(13500d);
        Mockito.when(serviceClass.generateEveryMonthPay(20000, 11.5, 6)).thenReturn(3716.67);
        Mockito.when(serviceClass.generateEveryMonthPay(5000, 10, 1)).thenThrow(new IllegalArgumentException());
        Mockito.when(serviceClass.generateEveryMonthPay(15000, 10, 1)).thenThrow(new IllegalArgumentException());
        Mockito.when(serviceClass.generateEveryMonthPay(15000, 2, 10)).thenThrow(new IllegalArgumentException());
    }
}