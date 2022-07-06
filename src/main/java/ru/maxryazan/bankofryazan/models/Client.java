package ru.maxryazan.bankofryazan.models;

import lombok.*;
import javax.persistence.*;
import java.util.HashMap;
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

    public Client(String firstName, String lastName, String hashPinCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pinCode = hashPinCode;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName + " phone: " + phoneNumber;
    }
}

