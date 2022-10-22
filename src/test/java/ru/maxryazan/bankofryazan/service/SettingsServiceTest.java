package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.maxryazan.bankofryazan.repository.SettingsRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SettingsServiceTest {

    @Mock
    private  ClientService clientService;

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private final SettingsService settingsService;

    SettingsServiceTest() {
        settingsService = new SettingsService(clientService, settingsRepository, passwordEncoder);
    }


    @Test
    void validatePasswords() {
        assertTrue(settingsService.validatePasswords("password",
                "password"));
        assertFalse(settingsService.validatePasswords("password",
                "wrongPassConfirm"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abac23g", "aAaa23g"})
    void validateNewPass(String value) {
        assertTrue(settingsService.validateNewPass(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abaccag", " aaa23g", "1234567"})
    void validateNewPass2(String value) {
        assertFalse(settingsService.validateNewPass(value));
    }

}