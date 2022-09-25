package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.models.Investment;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.repository.InvestmentRepository;


@Service
public class InvestmentService {

    private final ServiceClass serviceClass;
    private final RateService rateService;
    private final ExchangeRateClassService exchangeRateClassService;
    private final InvestmentRepository investmentRepository;

    public InvestmentService(ServiceClass serviceClass, RateService rateService,
                             ExchangeRateClassService exchangeRateClassService, InvestmentRepository investmentRepository) {
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

    private ExchangeRateClass createExchangeRateClass(int duration) {
        ExchangeRateClass rateClass = exchangeRateClassService.findByDate(serviceClass.generateDateMinusDays(duration));
        if(rateClass == null) {
            return exchangeRateClassService.findFirst();
        }
        return rateClass;
    }

    private Rate createRate(int duration) {
        Rate rate = rateService.findByDate(serviceClass.generateDateMinusDays(duration));
        if (rate == null) {
            return rateService.findFirst();
        }
        return rate;
    }

    private ExchangeRateClass createTempExchangeRateClass(ExchangeRateClass todayRate, ExchangeRateClass lastRate) {
        ExchangeRateClass exchangeRateClass = new ExchangeRateClass();
        exchangeRateClass.setCourse_USD(serviceClass.round(todayRate.getCourse_USD() - lastRate.getCourse_USD()));
        exchangeRateClass.setCourse_EUR(serviceClass.round(todayRate.getCourse_EUR() - lastRate.getCourse_EUR()));
        return exchangeRateClass;
    }

    private Rate createTempRate(Rate todayRate, Rate lastRate) {
        return new Rate(serviceClass.roundFloat(todayRate.getSilver() - lastRate.getSilver()),
                serviceClass.roundFloat(todayRate.getGold() - lastRate.getGold()),
        serviceClass.roundFloat(todayRate.getPalladium() - lastRate.getPalladium()),
        serviceClass.roundFloat(todayRate.getPlatinum() - lastRate.getPlatinum()),
        serviceClass.roundFloat(todayRate.getRhodium() - lastRate.getRhodium()));
    }

    public void save(Investment investment) {
        investmentRepository.save(investment);
    }

    public Rate findByDate(String date) {
    return rateService.showFromDB(date);
    }

    public ExchangeRateClass findByDateMoney(String date) {
        return exchangeRateClassService.findByDate(date);
    }

    public String changeType(String type) {
        String result = "";
        switch (type) {
            case "Золото" -> result = "gold";
            case "Серебро" -> result = "silver";
            case "Палладий" -> result = "palladium";
            case "Платина" -> result = "platinum";
            case "Родий" -> result = "rhodium";
            case "Доллар США" -> result = "USD";
            case "Евро" -> result = "EUR";
        }
       return result;
    }

    public double calculatePriceOfInvestment(String investment, String amount) {
        double result = 0;
        Rate rate = findByDate(serviceClass.generateDate());
        ExchangeRateClass exchangeRateClass = findByDateMoney(serviceClass.generateDate());
        System.out.println(rate);
        switch (investment) {
            case "gold" -> result = getResult(rate.getGold(), amount);
            case "silver" -> result = getResult(rate.getSilver(), amount);
            case "platinum" -> result = getResult(rate.getPlatinum(), amount);
            case "palladium" -> result = getResult(rate.getPalladium(), amount);
            case "rhodium" -> result = getResult(rate.getRhodium(), amount);
            case "USD" -> result = exchangeRateClass.getCourse_USD() * (Double.parseDouble(amount));
            case "EUR" -> result = exchangeRateClass.getCourse_EUR() * (Double.parseDouble(amount));
        }
       return serviceClass.round(result);
    }

    private double invokeAmount(String amount){
        return (Double.parseDouble(amount) / 28.35);
    }
    private double getResult(double ratePrice, String amount){
        return  ratePrice * invokeAmount(amount);
    }

    public void checkCurrPriceOfInvestment(Client client) {
        for (Investment inv : client.getInvestments()) {
            inv.setCurrPriceOfInvestment(calculatePriceOfInvestment(inv.getType(), Double.toString(inv.getInvestmentSizeByUnits())));
            inv.setMargin(serviceClass.round(inv.getCurrPriceOfInvestment() - inv.getBasePriceOfInvestment()));
            save(inv);
        }
    }

    public void createInvestment(String type, String amount, Client client) {
            String typeOfInvestment = changeType(type);
            double priceOfInvestment = calculatePriceOfInvestment(changeType(type), amount);

            Investment investment = new Investment();

            investment.setInvestor(client);
            investment.setDateOfInvestment(serviceClass.generateDate());
            investment.setType(typeOfInvestment);
            investment.setInvestmentSizeByUnits(Double.parseDouble(amount));
            investment.setBasePriceOfInvestment(priceOfInvestment);
            investment.setCurrPriceOfInvestment(priceOfInvestment);

            client.setBalance(client.getBalance() - priceOfInvestment);
            save(investment);
    }
}
