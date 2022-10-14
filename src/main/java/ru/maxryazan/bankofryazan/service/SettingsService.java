package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Settings;
import ru.maxryazan.bankofryazan.repository.SettingsRepository;

@Service
public class SettingsService {

      private final ClientService clientService;
      private final SettingsRepository settingsRepository;

    public SettingsService(ClientService clientService, SettingsRepository settingsRepository) {
        this.clientService = clientService;
        this.settingsRepository = settingsRepository;
    }


    public void save(Settings settings){
        settingsRepository.save(settings);
    }

    public boolean checkOpenPersonalAreaWithSecretCode(Client authClient){
        Settings settings = settingsRepository.findByAuthClient(authClient);
        return settings.getOpenPersonalAreaWithSecretCode().equals("1");
    }

    public boolean checkDoAllTransactionsWithSecretCode(Client authClient) {
        Settings settings = settingsRepository.findByAuthClient(authClient);
            return settings.getDoAllTransactionsWithSecretCode().equals("1");
    }

    public boolean checkLockAccessToPersonalAreaAfterThreeTry(String phoneNumber){
        Client authClient = clientService.findByPhoneNumber(phoneNumber);
        Settings settings = settingsRepository.findByAuthClient(authClient);
        return settings.getLockAccessToPersonalAreaAfterThreeTry().equals("1");
    }

    public boolean isCodesEquals(Client authClient, String codeFromClientForm) {
        String codeFromDB = authClient.getEmailCodes().get(authClient.getEmailCodes().size() - 1).getValue();
        return codeFromDB.equals(codeFromClientForm);
    }

    public boolean isTransactionalSecure(Client authClient) {
        return  checkDoAllTransactionsWithSecretCode(authClient);
    }
}
