package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.ExchangeRateClass;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateClass, Long> {

    ExchangeRateClass findByDate(String date);

    @Query(nativeQuery = true, value = "SELECT MIN(erc.id) FROM exchange_rate_class erc")
    Long findFirst();

}
