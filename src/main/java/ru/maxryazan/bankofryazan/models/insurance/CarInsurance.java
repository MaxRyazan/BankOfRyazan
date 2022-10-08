package ru.maxryazan.bankofryazan.models.insurance;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Status;

import javax.persistence.*;

@Entity
@Table(name = "osago")
@Getter
@NoArgsConstructor
public class CarInsurance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String dateOfCreation;
    private String dateOfExpired;
    private String carNumber;
    private int horsePower;
    private int yearOfCreation;
    private int numberOfDrivers;
    private String isATaxiCar;
    private double upperCoefficient;
    private double priceOfOsago;
    @Setter
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    public CarInsurance(final String dateOfCreation, final String dateOfExpired, final String carNumber,
                        final int horsePower, final int yearOfCreation, final int numberOfDrivers,
                        final String isATaxiCar, final double upperCoefficient, final double priceOfOsago,
                        final Client client) {
        this.dateOfCreation = dateOfCreation;
        this.dateOfExpired = dateOfExpired;
        this.carNumber = carNumber;
        this.horsePower = horsePower;
        this.yearOfCreation = yearOfCreation;
        this.numberOfDrivers = numberOfDrivers;
        this.isATaxiCar = isATaxiCar;
        this.upperCoefficient = upperCoefficient;
        this.priceOfOsago = priceOfOsago;
        this.client = client;
        this.status = Status.ACTIVE;
    }

    @Override
    public String toString() {
        return "CarInsurance{" +
                "dateOfExpired='" + dateOfExpired + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", priceOfOsago=" + priceOfOsago +
                '}';
    }

}

