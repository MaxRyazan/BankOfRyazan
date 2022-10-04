package ru.maxryazan.bankofryazan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import ru.maxryazan.bankofryazan.service.CarInsuranceService;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class InsuranceController {

    private final CarInsuranceService carInsuranceService;
    private final ServiceClass serviceClass;


    @GetMapping("/insurance")
    public String redirectToMainInsurance(){
        return "redirect:/insurance/main";
    }

    @GetMapping("/insurance/main")
    public String getInsuranceMainPage(){
        return "insurance/insurance-main";
    }

    @GetMapping("/insurance/osago")
    public String getIOsagoPage(@ModelAttribute(name = "koeff") String koeff,
                                @ModelAttribute(name = "price") String price){
        return "insurance/insurance-osago";
    }

    @PostMapping("insurance/osago")
    public String postOsagoPage(@RequestParam int horsePower,
                                @RequestParam int year,
                                @RequestParam int drivers,
                                @RequestParam String isTaxi, Model modelAttribute){
        double koeff = carInsuranceService.calculateCoefficientOfOsago(horsePower, year, drivers, isTaxi);
        double price = carInsuranceService.calculatePriceOfOsago(koeff);
        modelAttribute.addAttribute("koeff", serviceClass.round(koeff));
        modelAttribute.addAttribute("price", serviceClass.round(price));
        return "insurance/insurance-osago";
    }


    @GetMapping("/insurance/dms")
    public String getInsuranceDMSPage(){
        return "insurance/insurance-DMS";
    }
}
