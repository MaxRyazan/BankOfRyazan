package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Rate;
import java.util.List;

@Service
public class InvestmentService {

    private final ServiceClass serviceClass;
    private final RateService rateService;
    private final ExchangeRateClassService exchangeRateClassService;
    public InvestmentService(ServiceClass serviceClass, RateService rateService, ExchangeRateClassService exchangeRateClassService) {
        this.serviceClass = serviceClass;
        this.rateService = rateService;
        this.exchangeRateClassService = exchangeRateClassService;
    }

    public String createMainPage(Model model) {
        Rate thisDayRate = rateService.showFromDB(serviceClass.generateDate());
        model.addAttribute("thisDayRate", thisDayRate);

        ExchangeRateClass todayExchangeRate = exchangeRateClassService.getRateFromAPI();
        model.addAttribute("todayExchangeRate", todayExchangeRate);

        ExchangeRateClass exchangeRateADayAgo = exchangeRateClassService.findAll().stream()
                .filter(exchangeRateClass -> exchangeRateClass.getDate().equals(serviceClass.generateDateOfEndInDays(-1)))
                .findFirst().orElse(exchangeRateClassService.findAll().get(0)) ;
        ExchangeRateClass exchangeRateAWeekAgo = exchangeRateClassService.findAll().stream()
                .filter(exchangeRateClass -> exchangeRateClass.getDate().equals(serviceClass.generateDateOfEndInDays(-7)))
                .findFirst().orElse(exchangeRateClassService.findAll().get(0)) ;
        ExchangeRateClass exchangeRateAMonthAgo = exchangeRateClassService.findAll().stream()
                .filter(exchangeRateClass -> exchangeRateClass.getDate().equals(serviceClass.generateDateOfEndInDays(-31)))
                .findFirst().orElse(exchangeRateClassService.findAll().get(0)) ;
        ExchangeRateClass exchangeRateAYearAgo = exchangeRateClassService.findAll().stream()
                .filter(exchangeRateClass -> exchangeRateClass.getDate().equals(serviceClass.generateDateOfEndInDays(-365)))
                .findFirst().orElse(exchangeRateClassService.findAll().get(0)) ;

        ExchangeRateClass tempExchangeDay = new ExchangeRateClass(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_USD() - exchangeRateADayAgo.getCourse_USD()),
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_EUR() - exchangeRateADayAgo.getCourse_EUR()));
                model.addAttribute("dayExchange", tempExchangeDay);
        ExchangeRateClass tempExchangeWeek = new ExchangeRateClass(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_USD() - exchangeRateAWeekAgo.getCourse_USD()),
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_EUR() - exchangeRateAWeekAgo.getCourse_EUR()));
                model.addAttribute("weekExchange", tempExchangeWeek);
        ExchangeRateClass tempExchangeMonth = new ExchangeRateClass(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_USD() - exchangeRateAMonthAgo.getCourse_USD()),
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_EUR() - exchangeRateAMonthAgo.getCourse_EUR()));
                model.addAttribute("monthExchange", tempExchangeMonth);
        ExchangeRateClass tempExchangeYear = new ExchangeRateClass(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_USD() - exchangeRateAYearAgo.getCourse_USD()),
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayExchangeRate.getCourse_EUR() - exchangeRateAYearAgo.getCourse_EUR()));
                model.addAttribute("yearExchange", tempExchangeYear);



        List<Rate> rates = rateService.findAll();

        Rate rateADayAgo = rates.stream().filter(rate -> rate.getDate().equals(serviceClass.generateDateOfEndInDays(-1))).findFirst().orElse(rates.get(0));
        Rate rateAWeekAgo = rates.stream().filter(rate -> rate.getDate().equals(serviceClass.generateDateOfEndInDays(-7))).findFirst().orElse(rates.get(0));
        Rate rateAMonthAgo = rates.stream().filter(rate -> rate.getDate().equals(serviceClass.generateDateOfEndInDays(-31))).findFirst().orElse(rates.get(0));
        Rate rateAYearAgo = rates.stream().filter(rate -> rate.getDate().equals(serviceClass.generateDateOfEndInDays(-365))).findFirst().orElse(rates.get(0));

        Rate tempRateDay = new Rate(
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getSilver() - rateADayAgo.getSilver()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getGold() - rateADayAgo.getGold()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPalladium() - rateADayAgo.getPalladium()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPlatinum() - rateADayAgo.getPlatinum()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getRhodium() - rateADayAgo.getRhodium()));
        model.addAttribute("dayRate", tempRateDay);

        Rate tempRateWeek = new Rate(
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getSilver() - rateAWeekAgo.getSilver()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getGold() - rateAWeekAgo.getGold()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPalladium() - rateAWeekAgo.getPalladium()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPlatinum() - rateAWeekAgo.getPlatinum()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getRhodium() - rateAWeekAgo.getRhodium()));
        model.addAttribute("weekRate", tempRateWeek);

        Rate tempRateMonth = new Rate(
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getSilver() - rateAMonthAgo.getSilver()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getGold() - rateAMonthAgo.getGold()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPalladium() - rateAMonthAgo.getPalladium()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPlatinum() - rateAMonthAgo.getPlatinum()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getRhodium() - rateAMonthAgo.getRhodium()));
        model.addAttribute("monthRate", tempRateMonth);

        Rate tempRateYear = new Rate(
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getSilver() - rateAYearAgo.getSilver()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getGold() - rateAYearAgo.getGold()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPalladium() - rateAYearAgo.getPalladium()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getPlatinum() - rateAYearAgo.getPlatinum()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(thisDayRate.getRhodium() - rateAYearAgo.getRhodium()));
        model.addAttribute("yearRate", tempRateYear);

        return "/investments/investments-main";

    }
}
