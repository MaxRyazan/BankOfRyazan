package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.repository.ClientRepository;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @Mock
    ServiceClass serviceClass;
    @Mock
    ContributionService contributionService;
    @Mock
    InvestmentService investmentService;

    private ClientService clientService;

    @BeforeEach
    void setUp(){
        clientService = new ClientService(clientRepository, passwordEncoder, serviceClass,
                contributionService, investmentService);
    }


    @Test
    void save() {
        String firstName = "Max";
        String lastName = "Sidorov";
        String phoneNumber = "89505005050";
        String email = "max@mail.ru";
        String pinCode = "pass";

        Client client = new Client(firstName, lastName, phoneNumber, email, pinCode);
        given(clientRepository.findByPhoneNumber(any())).willReturn(null);

        clientService.save(firstName, lastName, phoneNumber, email, pinCode);

        assertThat(client.getBalance()).isEqualTo(0);
        assertThat(client.getBalanceEUR()).isEqualTo(0);
        assertThat(client.getBalanceUSD()).isEqualTo(0);

    }

    @Test
    void saveThrowEx() {
        String firstName = "Oleg";
        String lastName = "Smirnov";
        String phoneNumber = "89505005050";
        String email = "oleg@mail.ru";
        String pinCode = "pass";
        Client client = new Client(firstName, lastName,
                phoneNumber, email,pinCode);

        clientRepository.save(client);

        given(clientRepository.findByPhoneNumber(client.getPhoneNumber())).willReturn(client);

        assertThatThrownBy(() -> clientService.save(firstName, lastName, phoneNumber,
                email, pinCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("клиент с таким номером телефона уже зарегистрирован!");
    }

    @Test
    void validationPhoneNumberThrowEx() {
        String phoneNumber1 = "9505005050";
        String phoneNumber2 = "8950500505";
        String phoneNumber3 = "8950500505a";
        String phoneNumber4 = "8-9505005050";

        assertThatThrownBy(() -> clientService.validationPhoneNumber(phoneNumber1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> clientService.validationPhoneNumber(phoneNumber2))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> clientService.validationPhoneNumber(phoneNumber3))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> clientService.validationPhoneNumber(phoneNumber4))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findByPhoneNumber() {
        String firstName = "Oleg";
        String lastName = "Smirnov";
        String phoneNumber = "89505005050";
        String email = "oleg@mail.ru";
        String pinCode = "pass";
        Client client = new Client(firstName, lastName,
                phoneNumber, email,pinCode);
        given(clientRepository.findByPhoneNumber(phoneNumber)).willReturn(client);

        assertThat(clientService.findByPhoneNumber(client.getPhoneNumber())).isEqualTo(client);
    }

    @Test
    void findByPhoneNumberThrowEx() {
        String phone = "89507777777";

        assertThatThrownBy(() -> clientService.findByPhoneNumber(phone))
                .isInstanceOf(IllegalArgumentException.class);
    }

}