package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.repository.ExchangeRateRepository;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class ExchangeRateClassService {

    private final ExchangeRateRepository rateRepository;
    private final ServiceClass serviceClass;

    public ExchangeRateClassService(ExchangeRateRepository rateRepository, ServiceClass serviceClass) {
        this.rateRepository = rateRepository;
        this.serviceClass = serviceClass;
    }

    public ExchangeRateClass getRateFromAPI() {
        ExchangeRateClass exchangeRateClass = new ExchangeRateClass();
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<ExchangeRateClass> all = rateRepository.findAll();
            if (all.isEmpty() || !(all.get(all.size()-1).getDate().equals(simpleDateFormat.format(date)))) {
                try {
                    final String URL_EUR = "https://free.currconv.com/api/v7/convert?q=EUR_RUB&compact=ultra&apiKey=f197b54334ada744011e";
                    final String URL_USD = "https://free.currconv.com/api/v7/convert?q=USD_RUB&compact=ultra&apiKey=f197b54334ada744011e";
                    ExchangeRateClass EUR_exchange = mapper.readValue(new URL(URL_EUR), ExchangeRateClass.class);
                    ExchangeRateClass USD_exchange = mapper.readValue(new URL(URL_USD), ExchangeRateClass.class);
                    exchangeRateClass.setCourse_EUR(serviceClass.roundToDoubleWithTwoSymbolsAfterDot(EUR_exchange.getCourse_EUR()));
                    exchangeRateClass.setCourse_USD(serviceClass.roundToDoubleWithTwoSymbolsAfterDot(USD_exchange.getCourse_USD()));
                    exchangeRateClass.setDate(simpleDateFormat.format(date));
                    rateRepository.save(exchangeRateClass);
                    return exchangeRateClass;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if(all.get(all.size()- 1).getDate().equals(simpleDateFormat.format(date))) {
                    return all.get(all.size() -1);
                }
            }
            return null;
    }


    public List<ExchangeRateClass> findAll() {
        return rateRepository.findAll();
    }

}
