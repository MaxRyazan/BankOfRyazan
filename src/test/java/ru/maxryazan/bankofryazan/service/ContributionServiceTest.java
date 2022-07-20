package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ContributionServiceTest {

    ContributionService contributionService = Mockito.mock(ContributionService.class);

    @Test
    void checkEndDateOfContributions() {
    }

    @Test
    void generateDateOfEnd() {
        Mockito.when(contributionService.generateDateOfEnd(5)).thenReturn("20-12-2022");
        Mockito.when(contributionService.generateDateOfEnd(12)).thenReturn("20-07-2023");
    }

}