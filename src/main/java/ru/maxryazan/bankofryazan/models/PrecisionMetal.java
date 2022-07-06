package ru.maxryazan.bankofryazan.models;

import java.time.Instant;
import java.util.Date;

public class PrecisionMetal {

    private long id;

    private String name;

    double amount;

    private Date dateOfDeposit;

    private final int price;


    public PrecisionMetal(String name, Date dateOfDeposit, int price) {
        this.name = name;
        this.dateOfDeposit = Date.from(Instant.now());
        this.price = price; //TODO API для драгметаллов
    }
}
