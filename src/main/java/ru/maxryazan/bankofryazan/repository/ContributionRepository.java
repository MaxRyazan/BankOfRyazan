package ru.maxryazan.bankofryazan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxryazan.bankofryazan.models.Contribution;
@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {

     boolean existsByNumberOfContribution(String number);

}
