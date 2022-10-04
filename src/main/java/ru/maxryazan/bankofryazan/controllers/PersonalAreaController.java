package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import ru.maxryazan.bankofryazan.service.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@Controller
public class PersonalAreaController {
    private final ClientService clientService;
    private final ServiceClass serviceClass;
    private final TransactionalService transactionalService;
    private final CarInsuranceService carInsuranceService;

    public PersonalAreaController(ClientService clientService, ServiceClass serviceClass,
                                  TransactionalService transactionalService, CarInsuranceService carInsuranceService) {
        this.clientService = clientService;
        this.serviceClass = serviceClass;
        this.transactionalService = transactionalService;
        this.carInsuranceService = carInsuranceService;
    }


    @GetMapping("/main/personal-area")
    public String openPersonalArea(Model model, HttpServletRequest request,
                                   @ModelAttribute String error) {
            Client client = clientService.findByRequest(request);
      return  clientService.getPersonalPageArea(model, client);
    }


    @PostMapping("/main/personal-area")
    public String postTransaction(@RequestParam String recipientPhoneNumber,
                                  int sum, HttpServletRequest request, Model model) {
        model.addAttribute("client", clientService.findByRequest(request));
        if(!clientService.validationPhoneNumber(recipientPhoneNumber)){
            return serviceClass.showErrorMessage("Указан неверный номер телефона!",
                    "personal/personal", model);
        }
        if(clientService.ifSumNotValid(clientService.findByRequest(request).getPhoneNumber(), sum)){
            return serviceClass.showErrorMessage("Введена некорректная сумма!",
                    "personal/personal", model);
        }

        transactionalService.createNewTransaction(recipientPhoneNumber, sum, request);
        return "redirect:/main/personal-area";
    }

    @PostMapping("/buy_car_insurance")
    public String postBuyCarInsurance(@RequestParam String carNumber,
                                      @RequestParam int horsePower,
                                      @RequestParam int year,
                                      @RequestParam int drivers,
                                      @RequestParam String isTaxi,
                                      Model model,
                                      HttpServletRequest request){
        Client client = clientService.findByRequest(request);
        model.addAttribute(client);
        if(!carInsuranceService.validateYearOfCarBuild(year)){
            return serviceClass.showErrorMessage("Не верно указан год сборки авто! Укажите 4 цифры соответствующие году выпуска",
                    "personal/personal", model);
        }
        if(carInsuranceService.isInsuranceForThisCarAlreadyExist(carNumber)){
            return serviceClass.showErrorMessage("Машина с номером " + carNumber + " уже застрахована!",
                    "personal/personal", model);
        }
        if(!carInsuranceService.validateCaNumber(carNumber)){
            return serviceClass.showErrorMessage("Введен некорректный гос. номер! Маски номеров: а000аа / аа000 / аа0000",
                    "personal/personal", model);
        }
        double koeff = carInsuranceService.calculateCoefficientOfOsago(horsePower, year, drivers, isTaxi);
        double priceOfOsago = serviceClass.round(carInsuranceService.calculatePriceOfOsago(koeff));
        if(priceOfOsago > client.getBalance()){
            return serviceClass.showErrorMessage("Недостаточно средств!",
                    "personal/personal", model);
        }
        try {
            CarInsurance insurance = carInsuranceService.createCarInsurance(carNumber.toUpperCase(),
                    horsePower, year, drivers, isTaxi, koeff, client, priceOfOsago);
            clientService.updateBalance(client, -priceOfOsago);
            carInsuranceService.save(insurance);
            return serviceClass.showSuccessMessage("Спасибо что оформили полис ОСАГО у нас!",
                    "personal/personal", model);
        } catch (ParseException e){
            return serviceClass.showErrorMessage("Возникла непредвиденная ошибка, попробуйте еще раз",
                    "personal/personal", model);
        }
    }
}
