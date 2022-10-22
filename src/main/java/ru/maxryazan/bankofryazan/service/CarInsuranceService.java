package ru.maxryazan.bankofryazan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.models.accient_rate_api_response.ApiAccidentRateResponse;
import ru.maxryazan.bankofryazan.models.accient_rate_api_response.ClientDto;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import ru.maxryazan.bankofryazan.repository.CarInsuranceRepository;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@Service
public class CarInsuranceService {
    private final CarInsuranceRepository carInsuranceRepository;
    private final ServiceClass serviceClass;
    private final ClientService clientService;

    public CarInsuranceService(CarInsuranceRepository carInsuranceRepository, ServiceClass serviceClass, ClientService clientService) {
        this.carInsuranceRepository = carInsuranceRepository;
        this.serviceClass = serviceClass;
        this.clientService = clientService;

    }

    public void save(CarInsurance insurance){
        carInsuranceRepository.save(insurance);
    }

    public double calculateCoefficientOfOsago(int horsePower, int year, int drivers, String isTaxi) {
        double hp = validateHorsePowerCoefficient(horsePower);
        double yr = validateYearCoefficient(year);
        double dr = validateDriversCoefficient(drivers);
        double taxi = validateIsTaxiCoefficient(isTaxi);
        return serviceClass.round(1 + hp + yr + dr + taxi);
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

    public double calculatePriceOfOsago(double koeff, double accidentRate, int discount) {
        double basePriceOfOsago = 3000;
        var temp =  (koeff + accidentRate) * basePriceOfOsago;
        return serviceClass.round(temp - temp * ((double)discount * 0.01));
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

    public String checkInsuranceForExpiration(Client client) throws ParseException {
        if(!client.getCarInsurancies().isEmpty()) {
            for (CarInsurance ins : client.getCarInsurancies()) {
                if (serviceClass.afterDateOfEnd(ins.getDateOfExpired())) {
                    ins.setStatus(Status.CLOSED);
                    carInsuranceRepository.save(ins);
                    return "Страховка на автомобиль с гос. номером " + ins.getCarNumber() + " истекла!";
                }
            }
        }
        return "";
    }


    public String validateAllData(int year, String carNumber, int horsePower,
                                  int drivers, String isTaxi, Model model, HttpServletRequest request) {
        Client client = clientService.findByRequest(request);
        model.addAttribute(client);
        if (!validateYearOfCarBuild(year)) {
            return serviceClass.showErrorMessage("Не верно указан год сборки авто! Укажите 4 цифры соответствующие году выпуска",
                    "personal/personal", model);
        }
        if (isInsuranceForThisCarAlreadyExist(carNumber)) {
            return serviceClass.showErrorMessage("Машина с номером " + carNumber + " уже застрахована!",
                    "personal/personal", model);
        }
        if (!validateCaNumber(carNumber)) {
            return serviceClass.showErrorMessage("Введен некорректный гос. номер! Маски номеров: а000аа / аа000 / аа0000",
                    "personal/personal", model);
        }
        double koeff;
        double priceOfOsago;
        try {
            ClientDto responseValues = getAccidentRateFromApi(client.getFirstName(), client.getLastName(), client.getPatronymic());
            koeff = calculateCoefficientOfOsago(horsePower, year, drivers, isTaxi);
            priceOfOsago = serviceClass.round(calculatePriceOfOsago(koeff,
                    responseValues.getAccidentRate(),
                    responseValues.getDiscount()));
            if (priceOfOsago > client.getBalance()) {
                return serviceClass.showErrorMessage("Недостаточно средств!",
                        "personal/personal", model);
            }
        } catch (IOException e) {
            return serviceClass.showErrorMessage("Ошибка получения данных с API",
                    "personal/personal", model);
        }
        try {
            CarInsurance insurance = createCarInsurance(carNumber.toUpperCase(),
                    horsePower, year, drivers, isTaxi, koeff, client, priceOfOsago);
            clientService.updateBalance(client, -priceOfOsago);
            save(insurance);
            clientService.randerPersonalPage(client, model);
            return serviceClass.showSuccessMessage("Спасибо что оформили полис ОСАГО у нас!",
                    "personal/personal", model);
        } catch (Exception e){
            return serviceClass.showErrorMessage("Возникла непредвиденная ошибка, попробуйте еще раз",
                    "personal/personal", model);
        }
    }

    public ClientDto getAccidentRateFromApi(String firstName, String lastName, String patronymic) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = clientService.findByFLP(firstName, lastName, patronymic);

        final String URL = "http://localhost:8585/api/v1/" + client.getFirstName() + "/"
                + client.getPatronymic() + "/" + client.getLastName();
        if (objectMapper.readValue(new URL(URL), ApiAccidentRateResponse.class).getResponseCode().equals("200")) {
            ClientDto dto = objectMapper.readValue(new URL(URL), ApiAccidentRateResponse.class).getMessage();
            return new ClientDto(dto.getAccidentRate(), dto.getDiscount());
        }
        return new ClientDto(0,0);
    }

}
