package ru.maxryazan.bankofryazan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
@Component
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ExchangeRateClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private String date;

    @JsonProperty("EUR_RUB")
    @Column(name = "course_of_EUR")
    private double course_EUR;

    @JsonProperty("USD_RUB")
    @Column(name = "course_of_USD")
    private double course_USD;


    public ExchangeRateClass getRateFromAPI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final String URL_EUR = "https://free.currconv.com/api/v7/convert?q=EUR_RUB&compact=ultra&apiKey=f197b54334ada744011e";
        final String URL_USD = "https://free.currconv.com/api/v7/convert?q=USD_RUB&compact=ultra&apiKey=f197b54334ada744011e";
        ExchangeRateClass EUR_exchange = mapper.readValue(new URL(URL_EUR), ExchangeRateClass.class);
        ExchangeRateClass USD_exchange = mapper.readValue(new URL(URL_USD), ExchangeRateClass.class);
        ExchangeRateClass exchangeRateClass = new ExchangeRateClass();
        exchangeRateClass.setCourse_EUR(round(EUR_exchange.getCourse_EUR()));
        exchangeRateClass.setCourse_USD(round(USD_exchange.getCourse_USD()));
        return exchangeRateClass;
    }

    private double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }
}
