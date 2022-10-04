package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import ru.maxryazan.bankofryazan.repository.CarInsuranceRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CarInsuranceService {
    private final CarInsuranceRepository carInsuranceRepository;
    private final ServiceClass serviceClass;

    public CarInsuranceService(CarInsuranceRepository carInsuranceRepository, ServiceClass serviceClass) {
        this.carInsuranceRepository = carInsuranceRepository;
        this.serviceClass = serviceClass;
    }

    public void save(CarInsurance insurance){
        carInsuranceRepository.save(insurance);
    }

    public double calculateCoefficientOfOsago(int horsePower, int year, int drivers, String isTaxi) {
        double hp = validateHorsePowerCoefficient(horsePower);
        double yr = validateYearCoefficient(year);
        double dr = validateDriversCoefficient(drivers);
        double taxi = validateIsTaxiCoefficient(isTaxi);
        return 1 + hp + yr + dr + taxi;
    }

    private double validateIsTaxiCoefficient(String isTaxi) {
        if(isTaxi.equals("yes")) {
            return 0.2;
        }
        return 0;
    }

    private double validateDriversCoefficient(int drivers) {
       return switch (drivers){
            case 1  -> 0;
            case 2 -> 0.1;
            case 3 -> 0.15;
           default -> 0.42;
        };
    }

    public double validateYearCoefficient(int year) {
        String pattern = "yyyy";
        SimpleDateFormat simpleDate = new SimpleDateFormat(pattern);
        int now = Integer.parseInt(simpleDate.format(new Date()));
        int result = now - year;
        return switch (result) {
            case 0, 1 -> 0;
            case 2, 3 -> 0.11;
            case 4, 5 -> 0.15;
            default -> 0.32;
        };
    }

    public double validateHorsePowerCoefficient(int horsePower) {
        if(horsePower < 250) {
            if (isBetween(horsePower, 0, 100)){
                return 0;
            }
            if (isBetween(horsePower, 100, 125)){
                return 0.1;
            }
            if (isBetween(horsePower, 125, 149)){
                return 0.15;
            }
            if (isBetween(horsePower, 149, 199)){
                return 0.22;
            }
            if (isBetween(horsePower, 199, 249)){
                return 0.3;
            }
        }
            return 0.32;
    }

    private boolean isBetween(int horsePower, int low, int hight) {
        return horsePower > low && horsePower <= hight;
    }

    public double calculatePriceOfOsago(double koeff) {
        double basePriceOfOsago = 3000;
        return serviceClass.round(koeff * basePriceOfOsago);
    }

    public boolean validateYearOfCarBuild(int year) {
        if(Integer.toString(year).length() != 4){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int yearNow = Integer.parseInt(dateFormat.format(new Date()));
        return yearNow >= year;
    }

    public CarInsurance createCarInsurance(String carNumber, int horsePower, int year, int drivers, String isTaxi, double koeff,
                                           Client client, double priceOfOsago) throws ParseException {
        return new CarInsurance(serviceClass.generateDate(), serviceClass.generateDateMinusDays(365),
                carNumber, horsePower, year, drivers, isTaxi, koeff, priceOfOsago, client);
    }

    public boolean isInsuranceForThisCarAlreadyExist(String carNumber) {
        return carInsuranceRepository.existsByCarNumber(carNumber);
    }

    public boolean validateCaNumber(String carNumber) {
        String pattern = "[АВЕКМНОРСТУХABEKMHOPCTYX]\\d{3}[АВЕКМНОРСТУХABEKMHOPCTYX]{2}";
        String taxiPattern = "[АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{3}";
        String trailerPattern = "[АВЕКМНОРСТУХABEKMHOPCTYX]{2}\\d{4}";
        return carNumber.toUpperCase().replace(" ", "").matches(pattern) ||
                carNumber.toUpperCase().replace(" ", "").matches(taxiPattern) ||
                carNumber.toUpperCase().replace(" ", "").matches(trailerPattern);
    }
}
