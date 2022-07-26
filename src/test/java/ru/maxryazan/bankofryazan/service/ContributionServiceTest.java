package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ContributionServiceTest {

    ServiceClass serviceClass = Mockito.mock(ServiceClass.class);

    @Test
    void checkEndDateOfContributions() {
    }

    @Test
    void generateDateOfEnd() {
        Mockito.when(serviceClass.generateDateOfEnd(5)).thenReturn("20-12-2022");
        Mockito.when(serviceClass.generateDateOfEnd(12)).thenReturn("20-07-2023");
    }
}