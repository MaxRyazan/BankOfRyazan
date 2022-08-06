package ru.maxryazan.bankofryazan.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.*;
import ru.maxryazan.bankofryazan.repository.ClientRepository;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public final BCryptPasswordEncoder passwordEncoder;
    private final ServiceClass serviceClass;
    private final ContributionService contributionService;
    private final InvestmentService investmentService;

    public ClientService(ClientRepository clientRepository,
                         BCryptPasswordEncoder passwordEncoder, ServiceClass serviceClass,
                         ContributionService contributionService, InvestmentService investmentService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.serviceClass = serviceClass;
        this.contributionService = contributionService;
        this.investmentService = investmentService;
    }

    public void save(String firstName, String lastName, String phoneNumber, String email, String pinCode) {
        Client newClient = new Client(firstName, lastName,  validationPhoneNumber(phoneNumber), email, passwordEncoder.encode(pinCode));
        newClient.setBalance(0);
        newClient.setBalanceUSD(0);
        newClient.setBalanceEUR(0);
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
        String checkedPhone = phoneNumber.replace(" ", "");
        Optional<Client> cl = allClients.stream().filter(
                client -> client.getPhoneNumber().equals(checkedPhone)).findFirst();
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
        investmentService.checkCurrPriceOfInvestment(client);
        client.getEmailCodes().clear();
        for(Contribution cn : client.getContributions()) {
            if(cn.getStatus().equals(Status.ACTIVE)) {
                if (cn.getDateOfEnd().equals(serviceClass.generateDate())) {  //TODO добавить условие (дата ПОЗЖЕ чем текущая)
                    cn.setStatus(Status.CLOSED);
                    contributionService.save(cn);
                    cn.getContributor().setBalance(cn.getContributor().getBalance() + cn.getSumWithPercent());
                    save(cn.getContributor());
                }
            }
        }

        Collections.reverse(client.getInComingTransactions());
        Collections.reverse(client.getOutComingTransactions());

        model.addAttribute("firstName", client.getFirstName());
        model.addAttribute("lastName", client.getLastName());
        model.addAttribute("phone", client.getPhoneNumber());
        model.addAttribute("balance", serviceClass.roundToDoubleWithTwoSymbolsAfterDot(client.getBalance()));
        model.addAttribute("balanceUSD",  serviceClass.roundToDoubleWithTwoSymbolsAfterDot(client.getBalanceUSD()));
        model.addAttribute("balanceEUR",  serviceClass.roundToDoubleWithTwoSymbolsAfterDot(client.getBalanceEUR()));
        model.addAttribute("incoming", client.getInComingTransactions());
        model.addAttribute("outcoming", client.getOutComingTransactions());
        model.addAttribute("investments", client.getInvestments());

        List<Credit> closed = client.getCredits().stream()
                .filter(credit -> credit.getStatus().equals(Status.CLOSED)).collect(Collectors.toList());
        List<Credit> active = client.getCredits().stream()
                .filter(credit -> credit.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());

        Collections.reverse(closed);
        Collections.reverse(active);

        model.addAttribute("closedCredits", closed);
        model.addAttribute("activeCredits", active);

        List<Contribution> contributions = client.getContributions().stream()
                .filter(contribution -> contribution.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());

        Collections.reverse(contributions);

        model.addAttribute("contributions", contributions);
        save(client);
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
            Random random = new Random();
            String number;
            do {
                number = serviceClass.generateRandomUniqueNumber(random);
            } while (!isUnique(number));

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
        return contributionService.findAll().stream().noneMatch(contribution -> contribution.getNumberOfContribution().equals(str));
    }

    public void selfRegistration(String firstName, String lastName, String phoneNumber, String email, String pinCode) {
        Client client = new Client(firstName, lastName, phoneNumber, email, passwordEncoder.encode(pinCode));
        client.setBalance(0);
        client.setBalanceUSD(0);
        client.setBalanceEUR(0);
        clientRepository.save(client);
    }
}
