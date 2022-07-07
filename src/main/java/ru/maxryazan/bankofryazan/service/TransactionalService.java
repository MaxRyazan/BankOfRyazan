package ru.maxryazan.bankofryazan.service;


import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Transaction;
import ru.maxryazan.bankofryazan.repository.TransactionalRepository;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionalService {

    private final TransactionalRepository transactionalRepository;
    private final ClientService clientService;

    public TransactionalService(TransactionalRepository transactionalRepository, ClientService clientService) {
        this.transactionalRepository = transactionalRepository;
        this.clientService = clientService;
    }



    public Set<Transaction> findAllTransactionalWithRecipient(String recipientLastName, HttpServletRequest request) {
        Set<Transaction> setOfTransactions = new HashSet<>();
        Principal principal = request.getUserPrincipal();

        String  senderEmail = principal.getName();

        Client sender = clientService.findByPhoneNumber(senderEmail);
        List<Client> allClients = clientService.findAll();
        Optional<Client> recipient = allClients.stream().filter(client -> client.getLastName().equals(recipientLastName)).findFirst();
        if(recipient.isPresent()) {
            for(Transaction tr: recipient.get().getInComingTransactions()) {
                if((tr.getSender().getLastName()).equals(sender.getLastName())){
                    setOfTransactions.add(tr);
                }
            }
        }
        return setOfTransactions;
    }

    public void save(Transaction transaction) {
        transactionalRepository.save(transaction);
    }

    public void createNewTransaction(String recipientPhoneNumber, int sum, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String senderLastName = principal.getName();

        Client sender = clientService.findByPhoneNumber(senderLastName);
        Client recipient = clientService.findByPhoneNumber(recipientPhoneNumber);

        Date date = new Date();
        String pattern = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if (sender.getBalance() >= sum) {
            Transaction transaction = new Transaction(sender, recipient, sum, simpleDateFormat.format(date));
            sender.setBalance(sender.getBalance() - sum);
            recipient.setBalance(recipient.getBalance() + sum);
            transactionalRepository.save(transaction);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
