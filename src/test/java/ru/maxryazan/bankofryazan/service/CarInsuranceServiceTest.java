package ru.maxryazan.bankofryazan.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import ru.maxryazan.bankofryazan.repository.CarInsuranceRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CarInsuranceServiceTest {
    @Mock
     CarInsuranceRepository carInsuranceRepository;
    @Mock
    ClientService  clientService;
     ServiceClass serviceClass = new ServiceClass();


    CarInsuranceService carInsuranceService = new CarInsuranceService(carInsuranceRepository,
            serviceClass, clientService);


    @Test
    void validateYearOfCarBuild() {
        int year = 2022;
        assertTrue(carInsuranceService.validateYearOfCarBuild(year));
    }
    @Test
    void validateYearOfCarBuild2() {
        int year = 2023;
        assertFalse(carInsuranceService.validateYearOfCarBuild(year));
    }
    @Test
    void validateYearOfCarBuild3() {
        int year = 23;
        assertFalse(carInsuranceService.validateYearOfCarBuild(year));
    }

    @ParameterizedTest
    @ValueSource(strings = {"у907ох", "аа909",  "аа9099", "у 907 ох", "h 907 аt"})
    void validateCaNumber(String number) {
        assertTrue(carInsuranceService.validateCaNumber(number));
    }

}