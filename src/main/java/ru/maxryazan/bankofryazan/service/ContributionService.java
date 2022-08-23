package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Contribution;
import ru.maxryazan.bankofryazan.repository.ContributionRepository;

import java.util.List;

@Service
public class ContributionService {

    private final ContributionRepository contributionRepository;


    public ContributionService(ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }


    public void save(Contribution cn) {
        contributionRepository.save(cn);
    }


    public boolean existByNumberOfContribution(String number){
        return contributionRepository.existsByNumberOfContribution(number);
    }
}
