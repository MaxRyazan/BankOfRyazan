package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;

@Repository
public interface CarInsuranceRepository extends JpaRepository<CarInsurance, Long> {

    boolean existsByCarNumber(String carNumber);
}
