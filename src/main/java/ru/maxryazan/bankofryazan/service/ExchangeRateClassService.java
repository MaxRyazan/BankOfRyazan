package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.repository.ExchangeRateRepository;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


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
            if (rateRepository.findByDate(serviceClass.generateDate()) == null) {
                try {
                    final String URL_EUR = "https://free.currconv.com/api/v7/convert?q=EUR_RUB&compact=ultra&apiKey=f197b54334ada744011e";
                    final String URL_USD = "https://free.currconv.com/api/v7/convert?q=USD_RUB&compact=ultra&apiKey=f197b54334ada744011e";
                    ExchangeRateClass EUR_exchange = mapper.readValue(new URL(URL_EUR), ExchangeRateClass.class);
                    ExchangeRateClass USD_exchange = mapper.readValue(new URL(URL_USD), ExchangeRateClass.class);
                    exchangeRateClass.setCourse_EUR(serviceClass.roundToDoubleWithTwoSymbolsAfterDot(EUR_exchange.getCourse_EUR()));
                    exchangeRateClass.setCourse_USD(serviceClass.roundToDoubleWithTwoSymbolsAfterDot(USD_exchange.getCourse_USD()));
                    exchangeRateClass.setDate(serviceClass.generateDate());
                    rateRepository.save(exchangeRateClass);
                    return exchangeRateClass;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return rateRepository.findByDate(serviceClass.generateDate());
    }


    ExchangeRateClass findByDate(String date){
       return rateRepository.findByDate(date);
    }

    ExchangeRateClass findFirst(){
        Optional<ExchangeRateClass> rateClass =  rateRepository.findById(rateRepository.findFirst());
        return rateClass.orElse(null);
    }

}
