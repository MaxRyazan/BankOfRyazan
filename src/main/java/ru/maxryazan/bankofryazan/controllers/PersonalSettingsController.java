package ru.maxryazan.bankofryazan.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Settings;
import ru.maxryazan.bankofryazan.models.SystemMessage;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ServiceClass;
import ru.maxryazan.bankofryazan.service.SettingsService;
import ru.maxryazan.bankofryazan.service.SystemMessageService;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@Log4j2
@Controller
@RequiredArgsConstructor
public class PersonalSettingsController {

    private final ClientService clientService;
    private final SettingsService settingsService;
    private final ServiceClass serviceClass;

    private final SystemMessageService systemMessages;



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

    @GetMapping("/main/personal-area/settings/change_password")
    public String getChangePassPage(@ModelAttribute String error,
                                    @ModelAttribute String success){
        return "personal/change_password_page";
    }

    @PostMapping("/main/personal-area/settings/change_password")
    public String postChangePassPage(@RequestParam String oldPassword,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmNewPassword,
                                     HttpServletRequest request, Model model){
       Client client = clientService.findByRequest(request);
       if(settingsService.validateDataForChangePassword(client, oldPassword, newPassword, confirmNewPassword)) {
           try {
               settingsService.changePassword(newPassword, client);
               SystemMessage systemMessage = new SystemMessage(serviceClass.generateDate(), "Пароль был изменён!", client);
               systemMessages.save(systemMessage);
               return serviceClass.showSuccessMessage("Пароль успешно изменён!",
                       "/personal/change_password_page", model);
           } catch (UnknownHostException e){
               return serviceClass.showErrorMessage("Не удалось получить IP!",
                       "personal/change_password_page",model);
           }
       }
       return serviceClass.showErrorMessage("Данные для смены пароля не валидны!",
                "personal/change_password_page", model);
    }
}
