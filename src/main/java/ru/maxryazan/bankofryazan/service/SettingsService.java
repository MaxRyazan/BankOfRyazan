package ru.maxryazan.bankofryazan.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Settings;
import ru.maxryazan.bankofryazan.repository.SettingsRepository;

@Service
public class SettingsService {

      private final ClientService clientService;
      private final SettingsRepository settingsRepository;

      private final BCryptPasswordEncoder passwordEncoder;

    public SettingsService(ClientService clientService, SettingsRepository settingsRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.settingsRepository = settingsRepository;
        this.passwordEncoder = passwordEncoder;
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

    public boolean validateDataForChangePassword(Client client, String oldPassword, String newPassword, String confirmNewPassword) {
        return validateOldPass(client, oldPassword)
                && validateNewPass(newPassword)
                && validatePasswords(newPassword, confirmNewPassword);
    }

    public boolean validatePasswords(String newPassword, String confirmNewPassword) {
        return newPassword.equals(confirmNewPassword);
    }

    public boolean validateNewPass(String newPassword) {
        return !newPassword.trim().isBlank()
                && newPassword.trim().length() >= 7
                && !newPassword.trim().matches("\\d+")
                && !newPassword.trim().chars().allMatch(Character::isLetter);
    }

    public boolean validateOldPass(Client client, String oldPassword) {
        return passwordEncoder.matches(oldPassword, client.getPinCode());
    }

    public void changePassword(String newPassword, Client client) {
        client.setPinCode(passwordEncoder.encode(newPassword));
        clientService.save(client);
    }
}
