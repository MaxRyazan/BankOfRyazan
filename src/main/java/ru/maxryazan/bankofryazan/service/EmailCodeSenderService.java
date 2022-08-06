package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;
import ru.maxryazan.bankofryazan.repository.EmailCodeSenderRepository;
import java.util.Random;


@Service
public class EmailCodeSenderService {
    private final ClientService clientService;
    private final EmailCodeSenderRepository codeRepository;

    public EmailCodeSenderService(ClientService clientService, EmailCodeSenderRepository codeRepository) {
        this.clientService = clientService;
        this.codeRepository = codeRepository;
    }

    public String findByPhoneNumber(String phoneNumber) {
        Client client = clientService.findByPhoneNumber(phoneNumber);
        return client.getEmailCodes().get(client.getEmailCodes().size() - 1).getValue();
    }


    public String generateCode(String someString) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char[] array = someString.toCharArray();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
            if (i == random.nextInt(6) || i == random.nextInt(6)) {
                String up = String.valueOf(array[random.nextInt(array.length)]);
                sb.append(up.toUpperCase());
            } else {
                sb.append(array[random.nextInt(array.length)]);
            }
        }
        return sb.toString();
    }

    public void save(EmailCodeSender codeSender) {
        codeRepository.save(codeSender);
    }

    public void deleteAll(Client client) {
        for (EmailCodeSender ecs : codeRepository.findAll()) {
            if (ecs.getPassRestorer().getId() == client.getId()) {
                codeRepository.delete(ecs);
            }
        }
    }
}
