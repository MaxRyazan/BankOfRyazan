package ru.maxryazan.bankofryazan.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.Client;
import ru.maxryazan.bankofryazan.models.Contribution;
import ru.maxryazan.bankofryazan.models.Credit;
import ru.maxryazan.bankofryazan.models.Status;
import ru.maxryazan.bankofryazan.repository.ClientRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public record ClientService(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder,
                            ServiceClass serviceClass, ContributionService contributionService) {

    public void save(String firstName, String lastName, String phoneNumber, String email, String pinCode) {
        Client newClient = new Client(firstName, lastName,  validationPhoneNumber(phoneNumber), email, passwordEncoder.encode(pinCode));
        System.out.println("validatingPhone" + " 2");
        newClient.setBalance(0);
        clientRepository.save(newClient);
    }

    public String validationPhoneNumber(String phoneNumber){
        String validatingPhone = phoneNumber.replace(" ", "");
        String regex = "\\d+";
        if(
                validatingPhone.matches(regex)
                && (validatingPhone.length() == 11)
                && (validatingPhone.startsWith("8"))
        ) {
            return validatingPhone;
        }
        throw  new IllegalArgumentException();
    }

    public Client findByPhoneNumber(String phoneNumber) {
        List<Client> allClients = clientRepository.findAll();
        Optional<Client> cl = allClients.stream().filter(
                client -> client.getPhoneNumber().equals(phoneNumber)).findFirst();
        if (cl.isPresent()) {
            return cl.get();
        }
        throw new IllegalArgumentException("User with phone number: '" + phoneNumber + "' not found");
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findByRequest(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String authClientPhoneNumber = principal.getName();
        return findByPhoneNumber(authClientPhoneNumber);
    }

    public String getPersonalPageArea(Model model, HttpServletRequest request) {
        Client client = findByRequest(request);

        for(Contribution cn : client.getContributions()) {
            if(cn.getStatus().equals(Status.ACTIVE)) {
                if (cn.getDateOfEnd().equals(serviceClass.generateDate())) {
                    cn.setStatus(Status.CLOSED);
                    contributionService.save(cn);
                    cn.getContributor().setBalance(cn.getContributor().getBalance() + cn.getSumWithPercent());
                    save(cn.getContributor());
                }
            }
        }

        model.addAttribute("firstName", client.getFirstName());
        model.addAttribute("lastName", client.getLastName());
        model.addAttribute("phone", client.getPhoneNumber());
        model.addAttribute("balance", ((double)((int)(client.getBalance() * 100))) / 100);
        model.addAttribute("incoming", client.getInComingTransactions());
        model.addAttribute("outcoming", client.getOutComingTransactions());
        model.addAttribute("investments", client.getInvestments());
        Set<Credit> closed = client.getCredits().stream()
                .filter(credit -> credit.getStatus().equals(Status.CLOSED)).collect(Collectors.toSet());
        Set<Credit> active = client.getCredits().stream()
                .filter(credit -> credit.getStatus().equals(Status.ACTIVE)).collect(Collectors.toSet());
        model.addAttribute("closedCredits", closed);
        model.addAttribute("activeCredits", active);

        Set<Contribution> contributions = client.getContributions().stream()
                .filter(contribution -> contribution.getStatus().equals(Status.ACTIVE)).collect(Collectors.toSet());

        model.addAttribute("contributions", contributions);
        return "personal/personal";
    }

    /**
     * @param phoneNumber of current contributor
     * @param sum of contribution in roubles
     * @param percent of contribution
     * @param duration in MONTHS
     */
    public void addNewContribution(String phoneNumber, int sum, double percent, int duration) {
        Client client = findByPhoneNumber(phoneNumber);

        if(client.getBalance() >= sum) {

            Contribution contribution = new Contribution();
            String number;
            do {
                number = serviceClass.generateRandomUniqueNumber();
            }while (isUnique(number));
            contribution.setNumberOfContribution(number);
            contribution.setSumOfContribution(sum);
            contribution.setPercentByContribution(percent);
            contribution.setDurationOfContributionInYears(duration);
            contribution.setDateOfBegin(serviceClass.generateDate());
            contribution.setSumWithPercent(serviceClass.generateSumWithPercent(sum, percent, duration));
            contribution.setDateOfEnd(serviceClass.generateDateOfEndInMonth(duration));
            contribution.setStatus(Status.ACTIVE);
            contribution.setContributor(client);
            contributionService.save(contribution);
            client.setBalance(client.getBalance() - sum);
            save(client);
        } else {
            throw new IllegalArgumentException("not enough money");
        }
    }
    private boolean isUnique(String str) {
        for (Contribution cr : contributionService.findAll()) {
            if (cr.getNumberOfContribution().equals(str)) {
                return false;
            }
        }
        return true;
    }
}
