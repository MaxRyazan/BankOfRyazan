package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ServiceClassTest {

    ServiceClass serviceClass = new ServiceClass();
    @Test
    void generateEveryMonthPay() {
        int sum = 10000;
        double percent = 5d;
        int numbersOfPays = 10;
        double result = serviceClass.generateEveryMonthPay(sum, percent, numbersOfPays);
        assertThat(result).isEqualTo(1050.00);
          int sum2 = 11111;
          double percent2 = 7.1;
          int numbersOfPays2 = 7;
        double result2 = serviceClass.generateEveryMonthPay(sum2, percent2, numbersOfPays2);
        assertThat(result2).isEqualTo(1699.98);
    }

    @Test
    void generateSumWithPercent() {
        int sum1 = 7000;
        double percent1 = 5.3;
             int sum2 = 17010;
             double percent2 = 7.3;
        double result1 = serviceClass.generateSumWithPercent(sum1, percent1);
        double result2 = serviceClass.generateSumWithPercent(sum2, percent2);
        assertThat(result1).isEqualTo(7371d);
        assertThat(result2).isEqualTo(18251.73);
    }

    @Test
    void generateRandomUniqueNumber() {
        String pattern = "\\d+";

        String result = serviceClass.generateRandomUniqueNumber();
        assertThat(result.length()).isEqualTo(8);
        assertThat(result).matches(pattern);
    }

    @Test
    void generateDateWithHours() {
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String result = serviceClass.generateDateWithHours(new Date());
        assertThat(result).isEqualTo(simpleDateFormat.format(new Date()));
    }

    @Test
    void generateDate() {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String result = serviceClass.generateDate();
        assertThat(result).isEqualTo(simpleDateFormat.format(new Date()));
    }

    @Test
    void testGenerateSumWithPercent() {
        int sum1 = 10000;
        double percent1 = 7.1;
        int duration1 = 6;
           int sum2 = 11111;
           double percent2 = 7.1;
           int duration2 = 7;

        double result1 = serviceClass.generateSumWithPercent(sum1, percent1, duration1);
        double result2 = serviceClass.generateSumWithPercent(sum2, percent2, duration2);
         assertThat(result1).isEqualTo(10355d);
         assertThat(result2).isEqualTo(11571.18);
    }

    @Test
    void generateDateOfEndInMonth() throws ParseException {
        int duration = 1;
        String dateOfBeginFromDB = serviceClass.generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
        calendar.add(Calendar.MONTH, duration);

        assertThat(serviceClass.generateDateOfEndInMonth(duration))
                .isEqualTo(simpleDateFormat.format(calendar.getTime()));
    }

    @Test
    void generateDateOfEndInDays() throws ParseException {
        int duration = 1;
        String dateOfBeginFromDB = serviceClass.generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
        calendar.add(Calendar.DAY_OF_WEEK, duration);

        assertThat(serviceClass.generateDateMinusDays(duration))
                .isEqualTo(simpleDateFormat.format(calendar.getTime()));
    }

    @Test
    void afterDateOfEnd() throws ParseException {
        String dateOfEnd = "04-10-2022";
        assertTrue(serviceClass.afterDateOfEnd(dateOfEnd));
    }

    @Test
    void round() {
        assertEquals(14.11, serviceClass.round(14.11155));
        assertEquals(14.11, serviceClass.round(14.1185));
    }

    @Test
    void roundFloat() {
        assertEquals(14.11, serviceClass.round(14.11155));
        assertEquals(14.11, serviceClass.round(14.1185));
    }
}