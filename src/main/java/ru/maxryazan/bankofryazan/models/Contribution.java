package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "contribution")
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number_of_contribution", nullable = false)
    private String numberOfContribution;

    @Column(name = "sum", nullable = false)
    private int sumOfContribution;

    @Column(name = "percent", nullable = false)
    private double percentByContribution;

    @Column(name = "date_of_begin", nullable = false)
    private String dateOfBegin;

    @Column(name = "date_of_end", nullable = false)
    private String dateOfEnd;

    @Column(name = "duration", nullable = false)
    private int durationOfContributionInMonth;

    @Column(name = "sum_with_percent")
    private double sumWithPercent;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    private Client contributor;

    public Contribution(final String numberOfContribution,
                        final int sumOfContribution,
                        final double percentByContribution,
                        final String dateOfBegin,
                        String dateOfEnd,
                        int durationOfContributionInMonth,
                        double sumWithPercent,
                        Status status,
                        final Client contributor) {
        this.numberOfContribution = numberOfContribution;
        this.sumOfContribution = sumOfContribution;
        this.percentByContribution = percentByContribution;
        this.dateOfBegin = dateOfBegin;
        this.dateOfEnd = dateOfEnd;
        this.durationOfContributionInMonth = durationOfContributionInMonth;
        this.sumWithPercent = sumWithPercent;
        this.status = status;
        this.contributor = contributor;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "id=" + id +
                ", numberOfContribution='" + numberOfContribution + '\'' +
                ", sumOfContribution=" + sumOfContribution +
                ", percentByContribution=" + percentByContribution +
                ", dateOfBegin='" + dateOfBegin + '\'' +
                ", dateOfEnd='" + dateOfEnd + '\'' +
                ", durationOfContributionInYears=" + durationOfContributionInMonth +
                ", sumWithPercent=" + sumWithPercent +
                ", status=" + status +
                ", contributor=" + contributor +
                '}';
    }
}
