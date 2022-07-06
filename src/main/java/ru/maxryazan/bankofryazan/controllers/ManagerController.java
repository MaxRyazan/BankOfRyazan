package ru.maxryazan.bankofryazan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maxryazan.bankofryazan.service.ClientService;


@Controller
public class ManagerController {

    private final ClientService clientService;
    @Autowired
    public ManagerController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/manager")
    public String showManagerPage(){
        return "manager/manager-main-page";
    }

    @GetMapping("/manager/new-client")
    public String getAddNewClient(){
        System.out.println("get");
        return "manager/new-client";
    }

    @PostMapping("/manager/new-client")
    public String postAddNewClient(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   @RequestParam String pinCode){
        System.out.println("post");
        clientService.save(firstName, lastName, pinCode);
        return "redirect:/manager";
    }
}
