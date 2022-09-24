package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Credit;
@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    @Query("select c from Credit c where c.numberOfCreditContract = :contract")
    Credit findByNumberOfCreditContract(String contract);

    boolean existsByNumberOfCreditContract(String number);

    boolean existsById(long id);

    Credit findById(long id);
}
