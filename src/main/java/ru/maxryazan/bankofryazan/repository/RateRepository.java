package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Rate findByDate(String date);

    @Query(nativeQuery = true, value = "SELECT MIN(erc.id) FROM metal_rate erc")
    Long findFirst();


}
