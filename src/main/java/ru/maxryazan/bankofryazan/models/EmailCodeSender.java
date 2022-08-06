package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table (name = "email_code_sender")
public class EmailCodeSender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "restorer")
    private Client passRestorer;

    @Override
    public String toString() {
        return "EmailCodeSender{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", passRestorer=" + passRestorer +
                '}';
    }
}
