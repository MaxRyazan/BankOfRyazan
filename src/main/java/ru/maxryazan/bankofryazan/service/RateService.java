package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.MetalRate;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.repository.RateRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final ServiceClass serviceClass;

    public RateService(RateRepository rateRepository, ServiceClass serviceClass) {
        this.rateRepository = rateRepository;
        this.serviceClass = serviceClass;
    }

    public Rate getRateFromAPI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Rate rate;
                final String METALS = "https://metals-api.com/api/latest?access_key=ymg6zgqfz0m5emiz9niyfcumi5otq3mhq30i7nr73a6gw6jt1l31739ci076&base=RUB&symbols=XAU%2CXAG%2CXPD%2CXPT%2CXRH";
                MetalRate metals = mapper.readValue(new URL(METALS), MetalRate.class);
                rate = metals.getRates();
                rate.setDate(serviceClass.generateDate());
                rateRepository.save(rate);
                return rate;
    }

    public Rate showFromDB(String date) throws IOException {
        if(rateRepository.findByDate(date) == null){
            return getRateFromAPI();
        }
    return rateRepository.findByDate(date);
    }


    public Rate findByDate(String date) {
      return rateRepository.findByDate(date);
    }

    public Rate findFirst(){
        Optional<Rate> rate = rateRepository.findById(rateRepository.findFirst());
        return rate.orElse(null);
    }
}
