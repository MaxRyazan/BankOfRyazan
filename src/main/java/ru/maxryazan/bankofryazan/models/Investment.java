package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "date_of_investment")
    private String dateOfInvestment;

    @Column(name = "price_per_unit", nullable = false)
    double pricePerUnit;

    @Column(name = "investment_size_")
    private double investmentSizeByUnits;

    @Column(name = "base_price_of_investment")
    private double basePriceOfInvestment;

    @Column(name = "curr_price_of_investment")
    private double currPriceOfInvestment;

    @Column(name = "margin")
    private double margin;



    @ManyToOne
    @JoinColumn(name = "investment_id")
    private Client investor;



}
