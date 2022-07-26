package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.RateService;
import ru.maxryazan.bankofryazan.service.ServiceClass;


@Controller
@RequestMapping("/investments")
public class InvestmentController {
    private final ServiceClass serviceClass;

    private final RateService rateService;

    public InvestmentController(ServiceClass serviceClass, RateService rateService) {
        this.serviceClass = serviceClass;
        this.rateService = rateService;
    }

    @GetMapping("/main")
    public String getMainInvestments(Model model){
        Rate thisDayRate = rateService.showFromDB(serviceClass.generateDate());
        model.addAttribute("thisDayRate", thisDayRate);
        return "/investments/investments-main";
    }

    @GetMapping("/make")
    public String getMakeInvestment(){
        return "/investments/investments-make";
    }
}
