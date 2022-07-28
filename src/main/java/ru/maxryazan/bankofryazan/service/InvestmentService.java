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
        ExchangeRateClass todayExchangeRate = exchangeRateClassService.getRateFromAPI();
          model.addAttribute("thisDayRate", thisDayRate);
          model.addAttribute("todayExchangeRate", todayExchangeRate);

        ExchangeRateClass exchangeRateADayAgo = createExchangeRateClass(-1);
        ExchangeRateClass exchangeRateAWeekAgo = createExchangeRateClass(-7);
        ExchangeRateClass exchangeRateAMonthAgo = createExchangeRateClass(-31);
        ExchangeRateClass exchangeRateAYearAgo = createExchangeRateClass(-365);


        ExchangeRateClass tempExchangeDay = createTempExchangeRateClass(todayExchangeRate, exchangeRateADayAgo);
        ExchangeRateClass tempExchangeWeek = createTempExchangeRateClass(todayExchangeRate, exchangeRateAWeekAgo);
        ExchangeRateClass tempExchangeMonth = createTempExchangeRateClass(todayExchangeRate, exchangeRateAMonthAgo);
        ExchangeRateClass tempExchangeYear = createTempExchangeRateClass(todayExchangeRate, exchangeRateAYearAgo);
          model.addAttribute("dayExchange", tempExchangeDay);
          model.addAttribute("weekExchange", tempExchangeWeek);
          model.addAttribute("monthExchange", tempExchangeMonth);
          model.addAttribute("yearExchange", tempExchangeYear);


        Rate rateADayAgo = createRate(-1);
        Rate rateAWeekAgo = createRate(-7);
        Rate rateAMonthAgo = createRate(-31);
        Rate rateAYearAgo = createRate(-365);


        Rate tempRateDay = createTempRate(thisDayRate, rateADayAgo);
        Rate tempRateWeek = createTempRate(thisDayRate, rateAWeekAgo);
        Rate tempRateMonth = createTempRate(thisDayRate, rateAMonthAgo);
        Rate tempRateYear = createTempRate(thisDayRate, rateAYearAgo);
          model.addAttribute("dayRate", tempRateDay);
          model.addAttribute("weekRate", tempRateWeek);
          model.addAttribute("monthRate", tempRateMonth);
          model.addAttribute("yearRate", tempRateYear);

        return "/investments/investments-main";

    }

    private ExchangeRateClass createExchangeRateClass(int duration){
        return exchangeRateClassService.findAll().stream()
                .filter(exchangeRateClass -> exchangeRateClass.getDate().equals(serviceClass.generateDateOfEndInDays(duration)))
                .findFirst().orElse(exchangeRateClassService.findAll().get(0)) ;
    }
    private Rate createRate(int duration){
        List<Rate> rates = rateService.findAll();
        return rates.stream().filter(rate -> rate.getDate().equals(serviceClass.generateDateOfEndInDays(duration)))
                .findFirst().orElse(rates.get(0));
    }

    private ExchangeRateClass createTempExchangeRateClass(ExchangeRateClass todayRate, ExchangeRateClass lastRate){
        ExchangeRateClass exchangeRateClass = new ExchangeRateClass();
        exchangeRateClass.setCourse_USD(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getCourse_USD() - lastRate.getCourse_USD()));
        exchangeRateClass.setCourse_EUR(
                serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getCourse_EUR() - lastRate.getCourse_EUR()));
        return exchangeRateClass;
    }

    private Rate createTempRate(Rate todayRate, Rate lastRate){
        return new Rate(
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getSilver() - lastRate.getSilver()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getGold() - lastRate.getGold()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getPalladium() - lastRate.getPalladium()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getPlatinum() - lastRate.getPlatinum()),
                (float) serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(todayRate.getRhodium() - lastRate.getRhodium()));
    }
}
