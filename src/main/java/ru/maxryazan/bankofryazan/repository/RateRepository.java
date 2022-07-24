package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Rate findByDate(String date);
}
