package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ServiceClassTest {

    ServiceClass serviceClass = Mockito.mock(ServiceClass.class);

    @Test
    void generateEveryMonthPay() {
        Mockito.when(serviceClass.generateEveryMonthPay(1000, 5, 10)).thenReturn(105.0);
        Mockito.when(serviceClass.generateEveryMonthPay(1000, 5.5, 5)).thenReturn(211.0);
        Mockito.when(serviceClass.generateEveryMonthPay(1000, 10.15, 2)).thenReturn(550.75);
        Mockito.when(serviceClass.generateEveryMonthPay(1000000, 11.7, 36)).thenReturn(3027.78);
    }

    @Test
    void generateSumWithPercent() {
        Mockito.when(serviceClass.generateSumWithPercent(1000, 5)).thenReturn(1050d);
        Mockito.when(serviceClass.generateSumWithPercent(2217, 5.11)).thenReturn(2330.29);
        Mockito.when(serviceClass.generateSumWithPercent(10000, 11.3)).thenReturn(11130d);
        Mockito.when(serviceClass.generateSumWithPercent(1000000, 12.5)).thenReturn(1125000d);
    }

    @Test
    void generateRandomUniqueNumber() {

        class TestRandom extends Random{
        int base = 0;
        TestRandom(){
            super();
        }
        public int nextInt(){
            return base++;
        }
   }

        TestRandom testRandom = new TestRandom();
        Mockito.when(serviceClass.generateRandomUniqueNumber(testRandom)).thenReturn("01234567");
        testRandom.base = 1;
        Mockito.when(serviceClass.generateRandomUniqueNumber(testRandom)).thenReturn("12345678");
    }

    @Test
    void generateDateWithHours() {
    Date date = new Date(1659564000000L);
    Mockito.when(serviceClass.generateDateWithHours(date)).thenReturn("03-08-2022 22:00");
    }


    @Test
    void testGenerateSumWithPercent() {
        Mockito.when(serviceClass.generateSumWithPercent(10000, 6.2)).thenReturn(10620d);
        Mockito.when(serviceClass.generateSumWithPercent(100000, 10)).thenReturn(110000d);
    }

    @Test
    void generateDateOfEndInMonth() {
    }

    @Test
    void generateDateOfEndInDays() {
    }

    @Test
    void roundToDoubleWIthThreeSymbolsAfterDot() {
        Mockito.when(serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(10.44444444)).thenReturn(10.444);
        Mockito.when(serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(10.7777777)).thenReturn(10.778);
    }
}