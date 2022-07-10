package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "credit_number", unique = true, nullable = false)
    private String numberOfCreditContract;

    @Column(name = "credit_sum")
    private int sumOfCredit;

    @Column(name = "credit_percent")
    private double creditPercent;

    @Column(name = "date_of_begin")
    private String dateOfBegin;

    @Column(name = "number_of_pays")
    private int numberOfPays;

    @Column(name = "monthly_payment")
    private double everyMonthPay;

    @Column(name = "sum_with_percents")
    private final double sumWithPercents = everyMonthPay * numberOfPays;
    


    //TODO запилить сумму остатка по кредиту

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Client borrower;


}
