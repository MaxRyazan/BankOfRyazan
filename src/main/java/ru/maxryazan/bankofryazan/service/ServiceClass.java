package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class ServiceClass {
    public double generateEveryMonthPay(int sum, double percent, int numberOfPays) {
        long sumInKop = sum * 100L;
        double result = (sumInKop * (percent * 0.01) + sumInKop) / numberOfPays;
        int round = (int)result;
        return  (double)round / 100;
    }

    public double generateSumWithPercent(int sum, double percent) {
        long sumInKop = sum * 100L;
        return (sumInKop + sumInKop * (percent / 100)) / 100;
    }

    public String generateRandomUniqueNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(random.nextInt(10));
            }
        return sb.toString();
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

    public double generateSumWithPercent(int sum, double percent, int duration) {
        double percentByThisContributionDependsOfDuration = percent / 12 * duration * 0.01;
        return sum + sum * percentByThisContributionDependsOfDuration;
    }

    public String generateDateOfEndInMonth(int duration) {
        String dateOfBeginFromDB = generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
            calendar.add(Calendar.MONTH, duration);
            return simpleDateFormat.format(calendar.getTime());
        } catch (IllegalArgumentException | ParseException e) {
            e.printStackTrace();
        }
        throw  new IllegalArgumentException();
    }

    public String generateDateOfEndInDays(int duration) {
        String dateOfBeginFromDB = generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
            calendar.add(Calendar.DAY_OF_WEEK, duration);
            return simpleDateFormat.format(calendar.getTime());
        } catch (IllegalArgumentException | ParseException e) {
            e.printStackTrace();
        }
        throw  new IllegalArgumentException();
    }

    public double roundToDoubleWIthThreeSymbolsAfterDot(double number){
        return (double)((int)(number*1000)) / 1000;
    }
}
