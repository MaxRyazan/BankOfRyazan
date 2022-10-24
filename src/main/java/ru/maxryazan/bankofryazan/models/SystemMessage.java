package ru.maxryazan.bankofryazan.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Entity
@NoArgsConstructor
@Table(name = "System_message")
public class SystemMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private String date;

    @Column(name = "message")
    private String message;

    @Column(name = "IP_address")
    private String IP;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public SystemMessage(final String date, final String message, Client client, Type type) throws UnknownHostException {
        this.date = date;
        this.message = message;
        this.IP = String.valueOf(InetAddress.getLocalHost());
        this.client = client;
        this.type = type;
    }
}
