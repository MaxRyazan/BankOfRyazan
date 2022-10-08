package ru.maxryazan.bankofryazan.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Transaction;
import ru.maxryazan.bankofryazan.repository.TransactionalRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
public class TransactionalService {

    private final TransactionalRepository transactionalRepository;
    private final ClientService clientService;
    private final ServiceClass serviceClass;

    public TransactionalService(TransactionalRepository transactionalRepository, ClientService clientService, ServiceClass serviceClass) {
        this.transactionalRepository = transactionalRepository;
        this.clientService = clientService;
        this.serviceClass = serviceClass;
    }


    public void save(Transaction transaction) {
        transactionalRepository.save(transaction);
    }


    public void createNewTransaction(String recipientPhoneNumber,
                                     int sum, HttpServletRequest request) {

        Client sender = clientService.findByRequest(request);
        Client recipient = clientService.findByPhoneNumber(recipientPhoneNumber);

            Date date = new Date();
            Transaction transaction = new Transaction(
                    sender,
                    recipient,
                    sum,
                    serviceClass.generateDateWithHours(date)
            );

            clientService.updateBalance(sender, -sum);
            clientService.updateBalance(recipient, sum);
            transactionalRepository.save(transaction);

    }


    public String doTransaction(String recipientPhoneNumber,
                                int sum, HttpServletRequest request, Model model){
        model.addAttribute("client", clientService.findByRequest(request));
        if (!clientService.validationPhoneNumber(recipientPhoneNumber)) {
            return serviceClass.showErrorMessage("Указан неверный номер телефона!",
                    "personal/personal", model);
        }
        if (clientService.ifSumNotValid(clientService.findByRequest(request).getPhoneNumber(), sum)) {
            return serviceClass.showErrorMessage("Введена некорректная сумма!",
                    "personal/personal", model);
        }

        createNewTransaction(recipientPhoneNumber, sum, request);
        return "redirect:/main/personal-area";
    }
}
