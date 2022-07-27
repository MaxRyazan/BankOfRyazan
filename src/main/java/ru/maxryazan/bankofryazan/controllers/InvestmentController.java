package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.RateService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
    public String getMainInvestments(Model model) {
        List<Rate> rates = rateService.findAll();

        Rate thisDayRate = rateService.showFromDB(serviceClass.generateDate());

        model.addAttribute("thisDayRate", thisDayRate);
        model.addAttribute("goldPricePerGram", createPricePerGram(thisDayRate.getGold()));
        model.addAttribute("silverPricePerGram", createPricePerGram(thisDayRate.getSilver()));
        model.addAttribute("palladiumPricePerGram", createPricePerGram(thisDayRate.getPalladium()));
        model.addAttribute("platinumPricePerGram", createPricePerGram(thisDayRate.getPlatinum()));
        model.addAttribute("rhodiumPricePerGram", createPricePerGram(thisDayRate.getRhodium()));

        String stringDateMinusWeek = serviceClass.generateDateOfEnd(-7);
        String stringDateMinusMonth = serviceClass.generateDateOfEnd(-31);
        String stringDateMinusYear = serviceClass.generateDateOfEnd(-365);

        Rate rateAWeekAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusWeek)).findFirst().orElse(rates.get(0));
        Rate rateAMonthAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusMonth)).findFirst().orElse(rates.get(0));
        Rate rateAYearAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusYear)).findFirst().orElse(rates.get(0));

        for (Rate rateWeek : rates) {
            if (rateWeek.getDate().equals(rateAWeekAgo.getDate())) {
                model.addAttribute("goldDynamicByWeek", rateAWeekAgo.getGold() - thisDayRate.getGold());
                model.addAttribute("silverDynamicByWeek", rateAWeekAgo.getSilver() - thisDayRate.getSilver());
                model.addAttribute("palladiumDynamicByWeek", rateAWeekAgo.getPalladium() - thisDayRate.getPalladium());
                model.addAttribute("platinumDynamicByWeek", rateAWeekAgo.getPlatinum() - thisDayRate.getPlatinum());
                model.addAttribute("rhodiumDynamicByWeek", rateAWeekAgo.getRhodium() - thisDayRate.getRhodium());
            }
            for (Rate rateMonth : rates) {
                if (rateMonth.getDate().equals(rateAMonthAgo.getDate())) {
                    model.addAttribute("goldDynamicByMonth", rateAMonthAgo.getGold() - thisDayRate.getGold());
                    model.addAttribute("silverDynamicByMonth", rateAMonthAgo.getSilver() - thisDayRate.getSilver());
                    model.addAttribute("palladiumDynamicByMonth", rateAMonthAgo.getPalladium() - thisDayRate.getPalladium());
                    model.addAttribute("platinumDynamicByMonth", rateAMonthAgo.getPlatinum() - thisDayRate.getPlatinum());
                    model.addAttribute("rhodiumDynamicByMonth", rateAMonthAgo.getRhodium() - thisDayRate.getRhodium());
                }
                for (Rate rateYear : rates) {
                    if (rateYear.getDate().equals(rateAYearAgo.getDate())) {
                        model.addAttribute("goldDynamicByYear", rateAYearAgo.getGold() - thisDayRate.getGold());
                        model.addAttribute("silverDynamicByYear", rateAYearAgo.getSilver() - thisDayRate.getSilver());
                        model.addAttribute("palladiumDynamicByYear", rateAYearAgo.getPalladium() - thisDayRate.getPalladium());
                        model.addAttribute("platinumDynamicByYear", rateAYearAgo.getPlatinum() - thisDayRate.getPlatinum());
                        model.addAttribute("rhodiumDynamicByYear", rateAYearAgo.getRhodium() - thisDayRate.getRhodium());
                    }
                }
            }
        }

        return "/investments/investments-main";
    }

    @GetMapping("/make")
    public String getMakeInvestment(){
        return "/investments/investments-make";
    }

    public float createPricePerGram(float pricePerOunce){
        return  ((float)((int)(pricePerOunce / 28.3495 * 100000))) / 100000;
    }
}
