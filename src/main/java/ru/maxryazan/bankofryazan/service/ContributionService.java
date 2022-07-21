package ru.maxryazan.bankofryazan.service;

import org.springframework.stereotype.Service;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Contribution;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.ContributionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContributionService {

    private final ClientService clientService;
    private final CreditService creditService;
    private final ContributionRepository contributionRepository;

    public ContributionService(ClientService clientService,
                               CreditService creditService,
                               ContributionRepository contributionRepository) {
        this.clientService = clientService;
        this.creditService = creditService;
        this.contributionRepository = contributionRepository;
    }

    /**
     * @param phoneNumber of current contributor
     * @param sum of contribution in roubles
     * @param percent of contribution
     * @param duration in MONTHS
     */
    public void addNewContribution(String phoneNumber, int sum, double percent, int duration) {
        Client client = clientService.findByPhoneNumber(phoneNumber);

        if(client.getBalance() >= sum) {

            Contribution contribution = new Contribution();
              contribution.setNumberOfContribution(creditService.generateRandomUniqueNumber());
              contribution.setSumOfContribution(sum);
              contribution.setPercentByContribution(percent);
              contribution.setDurationOfContributionInYears(duration);
              contribution.setDateOfBegin(creditService.generateDate());
              contribution.setSumWithPercent(generateSumWithPercent(sum, percent, duration));
              contribution.setDateOfEnd(generateDateOfEnd(duration));
              contribution.setStatus(Status.ACTIVE);
              contribution.setContributor(client);
              contributionRepository.save(contribution);
                client.setBalance(client.getBalance() - sum);
            clientService.save(client);
        } else {
            throw new IllegalArgumentException("not enough money");
        }
    }

    /**
     * @param duration number of MONTH
     * @return date in String format of pattern ("dd-MM-yyyy")
     * @Checked by MockitoTest
     */
    public String generateDateOfEnd(int duration) {
        String dateOfBeginFromDB = creditService.generateDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(dateOfBeginFromDB));
            calendar.add(Calendar.MONTH, duration);
            return simpleDateFormat.format(calendar.getTime());
        } catch (IllegalArgumentException | ParseException e) {
            e.printStackTrace();
        }
            throw  new IllegalArgumentException();
    }

    /**
     * @param contributions current authUser set of contributions
     * @Description automatically checked date of the end for all contributions of current user.
     * Change balance - add sum of contribution with percents to current user's balance
     *
     */
    public void checkEndDateOfContributions(Set<Contribution> contributions) {
        for(Contribution cn : contributions) {
            if(cn.getStatus().equals(Status.ACTIVE)) {
                if (cn.getDateOfEnd().equals(creditService.generateDate())) {
                    cn.setStatus(Status.CLOSED);
                    contributionRepository.save(cn);
                    cn.getContributor().setBalance(cn.getContributor().getBalance() + cn.getSumWithPercent());
                    clientService.save(cn.getContributor());
                }
            }
        }
    }

    private double generateSumWithPercent(int sum, double percent, int duration) {
        double percentByThisContributionDependsOfDuration = percent / 12 * duration * 0.01;
        return sum + sum * percentByThisContributionDependsOfDuration;
    }
}
