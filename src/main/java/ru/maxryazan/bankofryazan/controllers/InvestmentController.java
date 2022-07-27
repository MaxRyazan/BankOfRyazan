package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.maxryazan.bankofryazan.models.Rate;
import ru.maxryazan.bankofryazan.service.RateService;
import ru.maxryazan.bankofryazan.service.ServiceClass;

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

        String stringDateMinusDay = serviceClass.generateDateOfEndInDays(-1);
        String stringDateMinusWeek = serviceClass.generateDateOfEndInDays(-7);
        String stringDateMinusMonth = serviceClass.generateDateOfEndInDays(-31);
        String stringDateMinusYear = serviceClass.generateDateOfEndInDays(-365);


        Rate rateADayAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusDay)).findFirst().orElse(rates.get(0));
        Rate rateAWeekAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusWeek)).findFirst().orElse(rates.get(0));
        Rate rateAMonthAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusMonth)).findFirst().orElse(rates.get(0));
        Rate rateAYearAgo = rates.stream().filter(rate -> rate.getDate().equals(stringDateMinusYear)).findFirst().orElse(rates.get(0));


        for (Rate rateDay : rates) {
            if (rateDay.getDate().equals(rateADayAgo.getDate())) {
                model.addAttribute("goldDynamicByDay",thisDayRate.getGold() - rateADayAgo.getGold());
                model.addAttribute("silverDynamicByDay", thisDayRate.getSilver() - rateADayAgo.getSilver());
                model.addAttribute("palladiumDynamicByDay", thisDayRate.getPalladium() - rateADayAgo.getPalladium());
                model.addAttribute("platinumDynamicByDay", thisDayRate.getPlatinum() - rateADayAgo.getPlatinum());
                model.addAttribute("rhodiumDynamicByDay", thisDayRate.getRhodium() - rateADayAgo.getRhodium());
            }
            for (Rate rateWeek : rates) {
                if (rateWeek.getDate().equals(rateAWeekAgo.getDate())) {
                    model.addAttribute("goldDynamicByWeek", thisDayRate.getGold() - rateAWeekAgo.getGold());
                    model.addAttribute("silverDynamicByWeek",thisDayRate.getSilver() - rateAWeekAgo.getSilver());
                    model.addAttribute("palladiumDynamicByWeek", thisDayRate.getPalladium() - rateAWeekAgo.getPalladium());
                    model.addAttribute("platinumDynamicByWeek",thisDayRate.getPlatinum() - rateAWeekAgo.getPlatinum());
                    model.addAttribute("rhodiumDynamicByWeek", thisDayRate.getRhodium() - rateAWeekAgo.getRhodium());
                }
                for (Rate rateMonth : rates) {
                    if (rateMonth.getDate().equals(rateAMonthAgo.getDate())) {
                        model.addAttribute("goldDynamicByMonth", thisDayRate.getGold() - rateAMonthAgo.getGold());
                        model.addAttribute("silverDynamicByMonth", thisDayRate.getSilver() - rateAMonthAgo.getSilver());
                        model.addAttribute("palladiumDynamicByMonth",thisDayRate.getPalladium() - rateAMonthAgo.getPalladium());
                        model.addAttribute("platinumDynamicByMonth", thisDayRate.getPlatinum() - rateAMonthAgo.getPlatinum());
                        model.addAttribute("rhodiumDynamicByMonth", thisDayRate.getRhodium() - rateAMonthAgo.getRhodium());
                    }
                    for (Rate rateYear : rates) {
                        if (rateYear.getDate().equals(rateAYearAgo.getDate())) {
                            model.addAttribute("goldDynamicByYear", thisDayRate.getGold() - rateAYearAgo.getGold());
                            model.addAttribute("silverDynamicByYear", thisDayRate.getSilver() - rateAYearAgo.getSilver());
                            model.addAttribute("palladiumDynamicByYear",  thisDayRate.getPalladium() - rateAYearAgo.getPalladium());
                            model.addAttribute("platinumDynamicByYear",thisDayRate.getPlatinum() - rateAYearAgo.getPlatinum());
                            model.addAttribute("rhodiumDynamicByYear",thisDayRate.getRhodium() - rateAYearAgo.getRhodium());
                        }
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
