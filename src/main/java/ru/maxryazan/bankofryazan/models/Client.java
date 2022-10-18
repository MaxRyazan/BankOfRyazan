package ru.maxryazan.bankofryazan.models;

import lombok.*;
import ru.maxryazan.bankofryazan.models.insurance.CarInsurance;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "patronymic", nullable = false)
    private String patronymic;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "balance", nullable = false)
    private double balance = 0;

    @Column(name = "balance_usd", nullable = false)
    private double balanceUSD = 0;

    @Column(name = "balance_eur", nullable = false)
    private double balanceEUR = 0;

    @Column(name = "hash_pin", nullable = false)
    private String pinCode;

    @OneToMany(mappedBy = "passRestorer", fetch = FetchType.LAZY)
    private List<EmailCodeSender> emailCodes;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Transaction> outComingTransactions;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<Transaction> inComingTransactions;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY)
    private List<Credit> credits;

    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY)
    private List<Investment> investments;

    @OneToMany(mappedBy = "contributor", fetch = FetchType.LAZY)
    private List<Contribution> contributions;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<CarInsurance> carInsurancies;

    @OneToOne(mappedBy = "authClient", fetch = FetchType.LAZY)
    private Settings settings;

    public Client(String firstName, String lastName, String patronymic, String email, String phoneNumber,
                  double balance, double balanceUSD, double balanceEUR, String pinCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.balanceUSD = balanceUSD;
        this.balanceEUR = balanceEUR;
        this.pinCode = pinCode;
        this.settings = new Settings(this,
                "0", "0", "0");
    }

    public double getBalance() {
        return (double) Math.round(balance * 100) / 100;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName + " " + patronymic;
    }


    public static class Builder{
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private double balance ;
        private double balanceUSD;
        private  double balanceEUR ;
        private String pinCode;


        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(final String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder balance() {
            this.balance = 0;
            return this;
        }

        public Builder balanceUSD() {
            this.balanceUSD = 0;
            return this;
        }

        public Builder balanceEUR() {
            this.balanceEUR = 0;
            return this;
        }

        public Builder pinCode(final String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public Client build(){
            return new Client();
        }
    }
}

