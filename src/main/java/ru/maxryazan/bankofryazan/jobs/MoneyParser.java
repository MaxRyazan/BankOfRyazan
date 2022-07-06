package ru.maxryazan.bankofryazan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
@Component
@Getter
@Setter
public class MoneyParser {
    @JsonProperty("EUR_RUB")
    public double course_EUR;
    @JsonProperty("USD_RUB")
    public double course_USD;


    public MoneyParser someMethod() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        final String URL_EUR = "https://free.currconv.com/api/v7/convert?q=EUR_RUB&compact=ultra&apiKey=f197b54334ada744011e";
        final String URL_USD = "https://free.currconv.com/api/v7/convert?q=USD_RUB&compact=ultra&apiKey=f197b54334ada744011e";
        MoneyParser parserEur = mapper.readValue(new URL(URL_EUR), MoneyParser.class);
        MoneyParser parserUsd = mapper.readValue(new URL(URL_USD), MoneyParser.class);
        MoneyParser moneyParser = new MoneyParser();
        moneyParser.setCourse_EUR(round(parserEur.getCourse_EUR()));
        moneyParser.setCourse_USD(round(parserUsd.getCourse_USD()));
        return moneyParser;
    }

    private double round(double a) {
        return (double) Math.round(a * 100) / 100;
    }
}
