package ru.maxryazan.bankofryazan.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.repository.ClientRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public record ClientService(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {


    public void save(String firstName, String lastName, String phoneNumber) {
        Client newClient = new Client(firstName, lastName, phoneNumber);
        newClient.setBalance(0);
        clientRepository.save(newClient);
        System.out.println("save");
    }

    public Client findByPhoneNumber(String phoneNumber) {
        List<Client> allClients = clientRepository.findAll();
        Optional<Client> cl = allClients.stream().filter(
                client -> client.getPhoneNumber().equals(phoneNumber)).findFirst();
        if (cl.isPresent()) {
            return cl.get();
        }
        throw new IllegalArgumentException("User with phone number: '" + phoneNumber + "' not found");
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findByRequest(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String authClientPhoneNumber = principal.getName();
        return findByPhoneNumber(authClientPhoneNumber);
    }
}
