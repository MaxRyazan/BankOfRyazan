package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Investment;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.repository.InvestmentRepository;
import java.util.List;

@Service
public class InvestmentService {

    private final ServiceClass serviceClass;
    private final RateService rateService;
    private final ExchangeRateClassService exchangeRateClassService;
    private final InvestmentRepository investmentRepository;
    public InvestmentService(ServiceClass serviceClass, RateService rateService, ExchangeRateClassService exchangeRateClassService, InvestmentRepository investmentRepository) {
        this.serviceClass = serviceClass;

        this.rateService = rateService;
        this.exchangeRateClassService = exchangeRateClassService;
        this.investmentRepository = investmentRepository;
    }



    public String createMainPage(Model model) {
        Rate thisDayRate = rateService.showFromDB(serviceClass.generateDate());
        ExchangeRateClass todayExchangeRate = exchangeRateClassService.getRateFromAPI();
          model.addAttribute("thisDayRate", thisDayRate);
          model.addAttribute("todayExchangeRate", todayExchangeRate);
          model.addAttribute("silver", "silver");


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

    public void save(Investment investment) {
            investmentRepository.save(investment);
    }

    public Rate findByDate(String date) {
        return rateService.findAll().stream().filter(rate -> rate.getDate().equals(date)).findFirst().orElse(null);
    }
    public ExchangeRateClass findByDateMoney(String date) {
        return exchangeRateClassService.findAll().stream().filter(rate -> rate.getDate().equals(date)).findFirst().orElse(null);
    }

    public String changeType(String type){
        switch (type) {
            case "Золото" -> {
                return "gold";
            }
            case "Серебро" -> {
                return "silver";
            }
            case "Палладий" -> {
                return "palladium";
            }
            case "Платина" -> {
                return "platinum";
            }
            case "Родий" -> {
                return "rhodium";
            }
            case "Доллар США" -> {
                return "USD";
            }
            case "Евро" -> {
                return "EUR";
            }
        }
        throw  new IllegalArgumentException();
    }

    public double setPriceOfInvestment(String investment, String amount){
        switch (investment) {
            case "gold" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDate(serviceClass.generateDate()).getGold() * (Double.parseDouble(amount) / 28.349));
            }
            case "silver" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDate(serviceClass.generateDate()).getSilver() * (Double.parseDouble(amount) / 28.349));
            }
            case "platinum" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDate(serviceClass.generateDate()).getPlatinum() * (Double.parseDouble(amount) / 28.349));
            }
            case "palladium" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDate(serviceClass.generateDate()).getPalladium() * (Double.parseDouble(amount) / 28.349));
            }
            case "rhodium" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDate(serviceClass.generateDate()).getRhodium()  * (Double.parseDouble(amount) / 28.349));
            }
            case "USD" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDateMoney(serviceClass.generateDate()).getCourse_USD()  * (Double.parseDouble(amount)));
            }
            case "EUR" -> {
                return serviceClass.roundToDoubleWIthThreeSymbolsAfterDot(findByDateMoney(serviceClass.generateDate()).getCourse_EUR()  * (Double.parseDouble(amount)));
            }
        }
        throw  new IllegalArgumentException();
    }

    public void checkCurrPriceOfInvestment(Client client){
        for(Investment inv : client.getInvestments()){
            setPriceOfInvestment(inv.getType(), Double.toString(inv.getInvestmentSizeByUnits()));
            save(inv);
        }
    }
}
