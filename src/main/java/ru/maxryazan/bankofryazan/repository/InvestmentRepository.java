package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Investment;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
