package ru.maxryazan.bankofryazan.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Table(name = "pay")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date")
    private String date;

    @Column(name = "sum")
    private double sum;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    public Pay(final String date, final double sum, final Credit credit) {
        this.date = date;
        this.sum = sum;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return
                "date=" + date +
                " sum=" + sum;
    }
}
