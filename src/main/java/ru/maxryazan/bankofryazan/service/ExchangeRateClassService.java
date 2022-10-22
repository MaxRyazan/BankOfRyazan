package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;
import ru.maxryazan.bankofryazan.repository.ExchangeRateRepository;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Log4j2
@Service
public class ExchangeRateClassService {

    private final ExchangeRateRepository rateRepository;
    private final ServiceClass serviceClass;

    public ExchangeRateClassService(ExchangeRateRepository rateRepository, ServiceClass serviceClass) {
        this.rateRepository = rateRepository;
        this.serviceClass = serviceClass;
    }

    public ExchangeRateClass getRateFromAPI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
            if (rateRepository.findByDate(serviceClass.generateDate()) == null) {
//                    final String URL = "https://openexchangerates.org/api/latest.json?app_id=5893314b8b434e2b932b101821c225fd";
                    final String URL_EUR = "https://free.currconv.com/api/v7/convert?q=EUR_RUB&compact=ultra&apiKey=6af10ed52db7073ded90";
                    final String URL_USD = "https://free.currconv.com/api/v7/convert?q=USD_RUB&compact=ultra&apiKey=6af10ed52db7073ded90";

                    ExchangeRateClass exchangeRateClass = new ExchangeRateClass(
                            mapper.readValue(new URL(URL_EUR), ExchangeRateClass.class).getCourse_EUR(),
                            mapper.readValue(new URL(URL_USD), ExchangeRateClass.class).getCourse_USD(),
                            serviceClass.generateDate()
                    );
                    rateRepository.save(exchangeRateClass);
                    return exchangeRateClass;
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
