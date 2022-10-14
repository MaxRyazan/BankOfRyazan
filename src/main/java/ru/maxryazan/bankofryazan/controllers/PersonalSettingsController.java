package ru.maxryazan.bankofryazan.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Settings;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.SettingsService;
import javax.servlet.http.HttpServletRequest;

@Log4j2
@Controller
@RequiredArgsConstructor
public class PersonalSettingsController {

    private final ClientService clientService;
    private final SettingsService settingsService;



    @GetMapping("/main/personal-area/settings")
    public String showSettingsPage(@ModelAttribute String message){
        return "personal/settings";
    }

    @PostMapping("/main/personal-area/settings")
    public String postSettingsPage(@RequestParam String allTransaction,
                                   HttpServletRequest request){
        Client authClient = clientService.findByRequest(request);
        Settings settings = authClient.getSettings();
        if(allTransaction.equals("yes")){
           settings.setDoAllTransactionsWithSecretCode("1");
        }
        if(allTransaction.equals("no")){
            settings.setDoAllTransactionsWithSecretCode("0");
        }
        settingsService.save(settings);
        clientService.save(authClient);
        return "redirect:/main/personal-area";
    }

}
