package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;

@Repository

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateClass, Long> {
}
