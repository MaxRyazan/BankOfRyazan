package ru.maxryazan.bankofryazan.models;


import ch.qos.logback.core.joran.spi.NoAutoStart;
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
    private String type;                        //gold, silver

    @Column(name = "unit_of_this_type_investment")
    private String unit;                        //gram, kilogram, piece

    @Column(name = "price_per_unit", nullable = false)
    double pricePerUnit;                         // 500 rouble per gram

    @Column(name = "investment_size_")
    private double investmentSizeByUnits;       // 1200 gram

    @Column(name = "price_of_investment")
    double priceOfInvestment;                   // 500 * 1200 = 600 000 roubles

    @Column(name = "date_of_investment")
    private String dateOfInvestment;

    @ManyToOne
    @JoinColumn(name = "investment_id")
    private Client investor;



}
