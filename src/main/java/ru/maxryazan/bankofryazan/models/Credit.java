package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


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

    @Column(name = "date_of_begin")
    private String dateOfBegin;

    @Column(name = "credit_percent")
    private double creditPercent;

    @Column(name = "sum_with_percents")
    private double sumWithPercents;

    @Column(name = "monthly_payment")
    private double everyMonthPay;

    @Column(name = "number_of_pays")
    private int numberOfPays;

    @Column(name = "rest_of_credit")
    private double restOfCredit;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private Client borrower;

    @OneToMany(mappedBy = "credit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pay> pays;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", numberOfCreditContract='" + numberOfCreditContract + '\'' +
                ", sumOfCredit=" + sumOfCredit +
                ", creditPercent=" + creditPercent +
                ", dateOfBegin='" + dateOfBegin + '\'' +
                ", numberOfPays=" + numberOfPays +
                ", everyMonthPay=" + everyMonthPay +
                ", sumWithPercents=" + sumWithPercents +
                ", borrower=" + borrower +
                ", pays=" + pays +
                '}';
    }
}
