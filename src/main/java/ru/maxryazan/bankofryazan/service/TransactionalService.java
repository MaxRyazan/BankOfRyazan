package ru.maxryazan.bankofryazan.service;


import org.springframework.stereotype.Service;
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



    public List<Transaction> findAllTransactionalWithRecipient(String recipientLastName, HttpServletRequest request) {
        Client sender = clientService.findByRequest(request);

        List<Transaction> listOfTransactions = new ArrayList<>(sender.getOutComingTransactions().stream()
                .filter(transaction -> transaction.getRecipient().getLastName().equals(recipientLastName)).toList());

        listOfTransactions.addAll(sender.getInComingTransactions().stream()
                .filter(transaction -> transaction.getSender().getLastName().equals(recipientLastName)).toList());

        return listOfTransactions;
    }




    public void save(Transaction transaction) {
        transactionalRepository.save(transaction);
    }

    public void createNewTransaction(String recipientPhoneNumber, int sum, HttpServletRequest request) {

        Client sender = clientService.findByRequest(request);
        Client recipient = clientService.findByPhoneNumber(recipientPhoneNumber);

        if (sender.getBalance() >= sum) {
            Date date = new Date();
            Transaction transaction = new Transaction(sender, recipient, sum, serviceClass.generateDateWithHours(date));
            sender.setBalance(sender.getBalance() - sum);
            recipient.setBalance(recipient.getBalance() + sum);
            transactionalRepository.save(transaction);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
