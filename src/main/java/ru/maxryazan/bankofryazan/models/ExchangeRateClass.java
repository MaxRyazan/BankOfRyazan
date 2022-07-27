package ru.maxryazan.bankofryazan.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Component
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "exchange_rate_class")
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

    public ExchangeRateClass(double course_EUR, double course_USD) {
        this.course_EUR = course_EUR;
        this.course_USD = course_USD;
    }

    @Override
    public String toString() {
        return "ExchangeRateClass{" +
                "date='" + date + '\'' +
                ", course_EUR=" + course_EUR +
                ", course_USD=" + course_USD +
                '}';
    }
}



