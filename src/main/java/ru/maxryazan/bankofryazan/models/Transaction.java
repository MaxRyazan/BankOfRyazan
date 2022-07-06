package ru.maxryazan.bankofryazan.models;

import lombok.*;
import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "sum")
    private int sum;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Client recipient;


    public Transaction(Client sender, Client recipient, int sum, String timestamp) {
        this.timestamp = timestamp;
        this.sum = sum;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Transaction № " + transactionId +
                "date and time: " + timestamp +
                "sum: " + sum +
                "sender: " + sender +
                "recipient: " + recipient;
    }
}
