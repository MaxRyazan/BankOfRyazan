package ru.maxryazan.bankofryazan.models;

import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //приватный номер клиента в БД
    @Column(name = "id")
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

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

    public Client(String firstName, String lastName, String phoneNumber, String email, String pinCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName;
    }
}

