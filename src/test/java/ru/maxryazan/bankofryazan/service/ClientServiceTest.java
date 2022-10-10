package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Contribution;
import ru.maxryazan.bankofryazan.models.EmailCodeSender;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.ClientRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    ServiceClass serviceClass;
    @Mock
    ContributionService contributionService;
    @Mock
    InvestmentService investmentService;
    ClientService clientService;

    @BeforeEach
    public void setup(){
        clientService = new ClientService(clientRepository,
                passwordEncoder, serviceClass, contributionService,
                investmentService);
    }

    @Test
    void validationPhoneNumber() {
    }

    @Test
    void randerPersonalPage() {
    }

    @Test
    void checkDateOfContributions() {
    }

    @Test
    void updateBalance() {
        Client client = new Client();
        client.setBalance(10000);

     clientService.updateBalance(client, 15000);
     assertEquals(25000, client.getBalance());
    }

    @Test
    void updateStatusAndBalance() throws ParseException {
        Contribution contribution = new Contribution();
        Client client = new Client();
        client.setBalance(10000);
        contribution.setStatus(Status.ACTIVE);
        contribution.setContributor(client);
        contribution.setSumWithPercent(10000);
        contribution.setDateOfEnd("10-10-2022");

        lenient().when(serviceClass.generateDate()).thenReturn("10-10-2022");

        clientService.updateStatusAndBalance(contribution);

        assertEquals(Status.CLOSED,  contribution.getStatus());
        assertEquals(20000, client.getBalance());
    }

    @Test
    void addNewContribution() {
    }

    @Test
    void existsByPhoneAndEmail() {
        Client client = new Client();
        client.setPhoneNumber("89505005050");
        client.setEmail("@");

        lenient().when(clientService.existsByPhoneNumber(client.getPhoneNumber())).thenReturn(true);
        lenient().when(clientService.existsByEmail(client.getEmail())).thenReturn(true);

        assertTrue(clientService.existsByPhoneAndEmail("89505005050",
           "@"));

    }
    @Test
    void existsByPhoneAndEmail2() {
        Client client = new Client();
        client.setPhoneNumber("89505005050");
        client.setEmail("@");

        lenient().when(clientService.existsByPhoneNumber(client.getPhoneNumber())).thenReturn(true);
        lenient().when(clientService.existsByEmail(client.getEmail())).thenReturn(false);

        assertTrue(clientService.existsByPhoneAndEmail("89505005050",
                "@"));

    }
    @Test
    void existsByPhoneAndEmail3() {
        Client client = new Client();
        client.setPhoneNumber("89505005050");
        client.setEmail("@");

        lenient().when(clientService.existsByPhoneNumber(client.getPhoneNumber())).thenReturn(false);
        lenient().when(clientService.existsByEmail(client.getEmail())).thenReturn(true);

        assertTrue(clientService.existsByPhoneAndEmail("89505005050",
                "@"));

    }
    @Test
    void existsByPhoneAndEmail4() {
        Client client = new Client();
        client.setPhoneNumber("89505005050");
        client.setEmail("@");

        lenient().when(clientService.existsByPhoneNumber(client.getPhoneNumber())).thenReturn(false);
        lenient().when(clientService.existsByEmail(client.getEmail())).thenReturn(false);

        assertFalse(clientService.existsByPhoneAndEmail("89505005050",
                "@"));

    }

    @Test
    void ifCodeFromEmailNotValid() {
        List<EmailCodeSender> codes = new ArrayList<>();
        EmailCodeSender code = new EmailCodeSender();
        code.setValue("emailCode");
        codes.add(code);

        Client client = new Client();
        client.setEmailCodes(codes);
        client.setPhoneNumber("89505005050");

        String codeFromEmail = code.getValue();

        lenient().when(clientService.findByPhoneNumber(client.getPhoneNumber())).thenReturn(client);
        assertFalse(clientService.ifCodeFromEmailNotValid(client.getPhoneNumber(),
                codeFromEmail));

    }
    @Test
    void ifCodeFromEmailNotValid2() {
        List<EmailCodeSender> codes = new ArrayList<>();
        EmailCodeSender code = new EmailCodeSender();
        code.setValue("emailCode");
        Client client = new Client();
        codes.add(code);
        client.setEmailCodes(codes);
        client.setPhoneNumber("89505005050");
        String codeFromEmail = "wrongString";

        lenient().when(clientService.findByPhoneNumber(client.getPhoneNumber())).thenReturn(client);

        assertTrue(clientService.ifCodeFromEmailNotValid(client.getPhoneNumber(),
                codeFromEmail));

    }

    @ParameterizedTest
    @ValueSource(ints = {-5000, 5000})
    void ifSumNotValid(int value) {
        Client client = new Client();
        client.setBalance(10000);
        client.setPhoneNumber("89505005050");
        lenient().when(clientService.findByPhoneNumber(client.getPhoneNumber())).thenReturn(client);
        assertFalse(clientService.ifSumNotValid(client.getPhoneNumber(), value));
    }

    @ParameterizedTest
    @ValueSource(ints = {-15000, 25000})
    void ifSumNotValid2(int value) {
        Client client = new Client();
        client.setBalance(10000);
        client.setPhoneNumber("89505005050");

        lenient().when(clientService.findByPhoneNumber(client.getPhoneNumber())).thenReturn(client);
        assertTrue(clientService.ifSumNotValid(client.getPhoneNumber(), value));
    }
}