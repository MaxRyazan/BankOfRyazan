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

    @Column(name = "course_EUR_sold")
    private double course_EUR_sold;

    @Column(name = "course_USD_sold")
    private double course_USD_sold;

    @Column(name = "percent_of_margin")
    private double percentOfMargin;

    public ExchangeRateClass(double course_EUR, double course_USD, String date) {
        this.percentOfMargin = 1.05;
        this.course_EUR = roundCourse(course_EUR);
        this.course_USD = roundCourse(course_USD);
        this.date = date;
        this.course_EUR_sold = roundCourse(course_EUR * percentOfMargin);
        this.course_USD_sold = roundCourse(course_USD * percentOfMargin);
    }

    @Override
    public String toString() {
        return String.format("Курс USD:\nПокупка: %s\nПродажа: %s.\nКурс EUR:\nПокупка: %s\nПродажа: %s.\n",
                course_USD,  course_EUR_sold, course_EUR, course_EUR_sold);
    }

    private double roundCourse(double number){
        return  (double)((int)(number * 100)) / 100;
    }
}



