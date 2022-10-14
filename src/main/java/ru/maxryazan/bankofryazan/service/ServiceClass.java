package ru.maxryazan.bankofryazan.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Log4j2
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

    public String generateDateWithHours(Date date ){
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
        double result = sum + sum * percentByThisContributionDependsOfDuration;
            return (double) Math.round(result * 100) / 100;
    }

    public String generateDateOfEndInMonth(int duration) throws ParseException {
        String dateOfBeginFromDB = generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
            calendar.add(Calendar.MONTH, duration);
            return simpleDateFormat.format(calendar.getTime());
    }

    public String generateDateMinusDays(int numberOfDays) throws ParseException {
        String dateOfBeginFromDB = generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
            calendar.add(Calendar.DAY_OF_WEEK, numberOfDays);
            log.info("[ServiceClass. public String generateDateMinusDays(int numberOfDays)] " + simpleDateFormat.format(calendar.getTime()));
        return simpleDateFormat.format(calendar.getTime());
    }


    public double round(double number){
        return (double)((int)(number * 100)) / 100;
    }

    public float roundFloat(double number){
        return (float)((int)(number * 100)) / 100;
    }


    public String showErrorMessage(String message, String pageToShow, Model model){
        model.addAttribute("error", message);
        return pageToShow;
    }

    public String showSuccessMessage(String message, String pageToShow, Model model) {
        model.addAttribute("success", message);
        return pageToShow;
    }

    public boolean afterDateOfEnd(String dateOfEnd) throws ParseException {
        Date now = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date endDate = dateFormat.parse(dateOfEnd);
        return endDate.before(now);
    }


}
