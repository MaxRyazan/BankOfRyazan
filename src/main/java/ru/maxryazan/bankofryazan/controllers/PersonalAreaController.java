package ru.maxryazan.bankofryazan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.service.ClientService;
import ru.maxryazan.bankofryazan.service.ServiceClass;
import ru.maxryazan.bankofryazan.service.TransactionalService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PersonalAreaController {
    private final ClientService clientService;
    private final ServiceClass serviceClass;
    private final TransactionalService transactionalService;

    public PersonalAreaController(ClientService clientService, ServiceClass serviceClass, TransactionalService transactionalService) {
        this.clientService = clientService;
        this.serviceClass = serviceClass;
        this.transactionalService = transactionalService;
    }


    @GetMapping("/main/personal-area")
    public String openPersonalArea(Model model, HttpServletRequest request, @ModelAttribute String error) {
      return  clientService.getPersonalPageArea(model, request);
    }


    @PostMapping("/main/personal-area")
    public String postTransaction(@RequestParam String recipientPhoneNumber,
                                  int sum, HttpServletRequest request, Model model) {
        model.addAttribute("client", clientService.findByRequest(request));
        if(!clientService.validationPhoneNumber(recipientPhoneNumber)){
            return serviceClass.showErrorMessage("Указан неверный номер телефона!", "personal/personal", model);
        }
        if(clientService.ifSumNotValid(clientService.findByRequest(request).getPhoneNumber(), sum)){
            return serviceClass.showErrorMessage("Введена некорректная сумма!", "personal/personal", model);
        }

        transactionalService.createNewTransaction(recipientPhoneNumber, sum, request);
        return "redirect:/main/personal-area";
    }
}
