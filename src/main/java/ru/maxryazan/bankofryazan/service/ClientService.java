package ru.maxryazan.bankofryazan.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.maxryazan.bankofryazan.models.*;
import ru.maxryazan.bankofryazan.repository.ClientRepository;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Client client = new Client.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .email(email)
                .pinCode(passwordEncoder.encode(pinCode))
                .build();
        clientRepository.save(client);
    }

    public boolean existsByPhoneNumber(String phone) {
        return clientRepository.existsByPhoneNumber(phone);
    }

    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }


    public boolean validationPhoneNumber(String phoneNumber) {
        String validatingPhone = phoneNumber.replace(" ", "");
        String regex = "\\d+";
        return validatingPhone.matches(regex)
                && (validatingPhone.length() == 11)
                && (validatingPhone.startsWith("8"));

    }

    public Client findByPhoneNumber(String phoneNumber) {
        if (validationPhoneNumber(phoneNumber)) {
            return clientRepository.findByPhoneNumber(phoneNumber);
        }
        return null;
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
        checkDateOfContributions(client);

        Collections.reverse(client.getInComingTransactions());
        Collections.reverse(client.getOutComingTransactions());

        List<Credit> closed = sortCreditsByStatus(client, Status.CLOSED);
        List<Credit> active = sortCreditsByStatus(client, Status.ACTIVE);
        List<Contribution> contributions = getActiveContributions(client);

        model.addAttribute("contributions", contributions);
        model.addAttribute("client", client);
        model.addAttribute("closedCredits", closed);
        model.addAttribute("activeCredits", active);
        save(client);
        return "personal/personal";
    }

    private List<Credit> sortCreditsByStatus(Client client, Status status) {
        List<Credit> result = new ArrayList<>(
                client.getCredits().stream().filter(credit -> credit.getStatus().equals(status)).toList()
        );
        Collections.reverse(result);
        return result;
    }

    private List<Contribution> getActiveContributions(Client client) {
        List<Contribution> result = new ArrayList<>(
                client.getContributions().stream().filter(Contribution -> Contribution.getStatus().equals(Status.ACTIVE)).toList()
        );
        Collections.reverse(result);
        return result;
    }

    private void checkDateOfContributions(Client client) {
        client.getContributions().forEach(this::updateStatusAndBalance);
    }

    public void updateBalance(Client client, double sum) {
        double newBalance = client.getBalance() + sum;
        client.setBalance(newBalance);
        save(client);
    }

    private void updateStatusAndBalance(Contribution cn) {
        if (cn.getStatus().equals(Status.ACTIVE)) {
            if (cn.getDateOfEnd().equals(serviceClass.generateDate()) || afterDateOfEnd(cn.getDateOfEnd())) {
                cn.setStatus(Status.CLOSED);
                contributionService.save(cn);
                updateBalance(cn.getContributor(), cn.getSumWithPercent());
            }
        }
    }

    private boolean afterDateOfEnd(String dateOfEnd) {
        Date now = new Date();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date endDate = dateFormat.parse(dateOfEnd);
            return endDate.before(now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param phoneNumber of current contributor
     * @param sum         of contribution in roubles
     * @param percent     of contribution
     * @param duration    in MONTHS
     */
    public void addNewContribution(String phoneNumber, int sum, double percent, int duration) {
        Client client = findByPhoneNumber(phoneNumber);
            String number;
            do {
                number = serviceClass.generateRandomUniqueNumber();
            } while (isUnique(number));

            Contribution contribution = new Contribution(
                    number,
                    sum,
                    percent,
                    serviceClass.generateDate(),
                    serviceClass.generateDateOfEndInMonth(duration),
                    duration,
                    serviceClass.generateSumWithPercent(sum, percent, duration),
                    Status.ACTIVE,
                    client
            );

            contributionService.save(contribution);
            updateBalance(client, -sum);
    }

    private boolean isUnique(String str) {
        return !contributionService.existByNumberOfContribution(str);
    }


    public boolean existsByPhoneAndEmail(String phoneNumber, String email) {
        if (validationPhoneNumber(phoneNumber)) {
            return existsByPhoneNumber(phoneNumber) && existsByEmail(email);
        }
        return false;
    }

    public boolean ifCodeFromEmailNotValid(String phoneNumber, String codeFromEmail) {
        Client client = findByPhoneNumber(phoneNumber);
        String actualCode = client.getEmailCodes().get(0).getValue();
        return !actualCode.equals(codeFromEmail);
    }

    public boolean ifSumNotValid(String phone, int sum) {
        Client client = findByPhoneNumber(phone);
        return client.getBalance() < sum;
    }
}


