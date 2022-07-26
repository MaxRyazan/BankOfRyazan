package ru.maxryazan.bankofryazan.models;

import lombok.*;
import javax.persistence.*;
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

    @Column(name = "hash_pin", nullable = false)
    private String pinCode;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Transaction> outComingTransactions;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private Set<Transaction> inComingTransactions;

    @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY)
    private Set<Credit> credits;

    @OneToMany(mappedBy = "investor", fetch = FetchType.LAZY)
    private Set<Investment> investments;

    @OneToMany(mappedBy = "contributor", fetch = FetchType.LAZY)
    private Set<Contribution> contributions;

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

