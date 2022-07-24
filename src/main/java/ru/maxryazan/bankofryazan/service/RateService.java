package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.MetalRate;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.repository.RateRepository;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final CreditService creditService;
    public RateService(RateRepository rateRepository, CreditService creditService) {
        this.rateRepository = rateRepository;
        this.creditService = creditService;
    }

    public Rate getRateFromAPI() {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        List<Rate> all = rateRepository.findAll();

        if (all.isEmpty() || !(all.get(all.size() - 1).getDate().equals(simpleDateFormat.format(date)))) {
            try {
                final String METALS = "https://metals-api.com/api/latest?access_key=ymg6zgqfz0m5emiz9niyfcumi5otq3mhq30i7nr73a6gw6jt1l31739ci076&base=RUB&symbols=XAU%2CXAG%2CXPD%2CXPT%2CXRH";
                MetalRate metals = mapper.readValue(new URL(METALS), MetalRate.class);
               Rate rate = metals.getRates();
               rate.setDate(creditService.generateDate());
                rateRepository.save(rate);
                return metals.getRates();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if(all.get(all.size()-1).getDate().equals(simpleDateFormat.format(date))) {
                return all.get(all.size() - 1);
            }
        }
        return null;
    }

    public Rate showFromDB(String date){
        Optional<Rate> thisRate = rateRepository.findAll().stream().filter(rate -> rate.getDate().equals(date)).findFirst();
        return thisRate.orElseGet(this::getRateFromAPI);
    }

}
