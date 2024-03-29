package ru.maxryazan.bankofryazan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;

@Service
public class MailSender {
   private final JavaMailSender mailSender;
   private final ClientService clientService;
   private final EmailCodeSenderService emailCodeSenderService;

    public MailSender(JavaMailSender mailSender, ClientService clientService, EmailCodeSenderService emailCodeSenderService) {
        this.mailSender = mailSender;
        this.clientService = clientService;
        this.emailCodeSenderService = emailCodeSenderService;
    }
    @Value("${spring.mail.username}")
    private String username;

    public  void send(String emailTo, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }


    public void restorePassword(String email, String phoneNumber) {
        Client client = clientService.findByPhoneNumber(phoneNumber);
        if(client.getEmail().equals(email.replace(" ", ""))){
            String subject = "Password restore";
            String code = emailCodeSenderService.generateCode(client.getLastName());
            String message = "Your code for password restore : " + code;
            send(email, subject, message);

            EmailCodeSender codeSender = new EmailCodeSender();
            codeSender.setValue(code);
            codeSender.setPassRestorer(client);
            client.getEmailCodes().add(codeSender);
            emailCodeSenderService.save(codeSender);
        }
    }

    public void sendCodeToEmail(Client authClient) {
            String subject = "Password for transaction";
            String code = emailCodeSenderService.generateCode(authClient.getLastName());
            String message = "Your code for transactions : " + code;
            send(authClient.getEmail(), subject, message);

            EmailCodeSender codeSender = new EmailCodeSender();
            codeSender.setValue(code);
            codeSender.setPassRestorer(authClient);
            authClient.getEmailCodes().add(codeSender);
            emailCodeSenderService.save(codeSender);
    }

    public void resetPassword(String phoneNumber, String password) {
        Client client = clientService.findByPhoneNumber(phoneNumber);
            client.setPinCode(clientService.passwordEncoder.encode(password));
            clientService.save(client);
            emailCodeSenderService.deleteAllCodesOFThisRestorer(client);
    }
}
