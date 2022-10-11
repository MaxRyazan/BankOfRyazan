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
import ru.maxryazan.bankofryazan.models.*;
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

    @ParameterizedTest
    @ValueSource(strings = {"89505005050", "89237421524", "8 950 500 50 50"})
    @DisplayName("Тестируем подходящие номера")
    void validationPhoneNumber(String value) {
        assertTrue(clientService.validationPhoneNumber(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"+79505005050", "9237421524", "+79509505050"})
    @DisplayName("Тестируем НЕПОДХОДЯЩИЕ номера")
    void validationPhoneNumber2(String value) {
        assertFalse(clientService.validationPhoneNumber(value));
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

    @Test
    void sortCreditsByStatus() {
        Credit credit1 = new Credit();
            credit1.setStatus(Status.ACTIVE);
            credit1.setNumberOfCreditContract("1");
        Credit credit2 = new Credit();
            credit2.setStatus(Status.ACTIVE);
        credit2.setNumberOfCreditContract("2");
        Credit credit3 = new Credit();
            credit3.setStatus(Status.CLOSED);
        credit3.setNumberOfCreditContract("3");
        Credit credit4 = new Credit();
            credit4.setStatus(Status.CLOSED);
        credit4.setNumberOfCreditContract("4");
        Client client = new Client();
        List<Credit> credits = new ArrayList<>();
            credits.add(credit1);
            credits.add(credit2);
            credits.add(credit3);
            credits.add(credit4);
            client.setCredits(credits);

        List<Credit> listOfActive = new ArrayList<>();
        listOfActive.add(credit2);
        listOfActive.add(credit1);
        List<Credit> listOfClosed = new ArrayList<>();
        listOfClosed.add(credit4);
        listOfClosed.add(credit3);

        assertEquals(listOfActive, clientService.sortCreditsByStatus(client, Status.ACTIVE));
        assertEquals(listOfClosed, clientService.sortCreditsByStatus(client, Status.CLOSED));

    }

    @Test
    void getActiveContributions() {
        Contribution contribution1 = new Contribution();
        Contribution contribution2 = new Contribution();
        contribution1.setStatus(Status.ACTIVE);
        contribution2.setStatus(Status.CLOSED);

        List<Contribution> contributions = new ArrayList<>();
        contributions.add(contribution1);
        contributions.add(contribution2);

        List<Contribution> active = new ArrayList<>();
        active.add(contribution1);

        Client client = new Client();
        client.setContributions(contributions);

        assertEquals(active , clientService.getActiveContributions(client));

    }
}