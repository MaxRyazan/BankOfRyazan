package ru.maxryazan.bankofryazan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BankOfRyazanApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankOfRyazanApplication.class, args);
    }

}
