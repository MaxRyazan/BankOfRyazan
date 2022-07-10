package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number_of_contribution", nullable = false)
    private String numberOfContribution;

    @Column(name = "sum", nullable = false)
    private int sumOfContribution;

    @Column(name = "percent", nullable = false)
    private int percentByContribution;

    @Column(name = "date_of_begin", nullable = false)
    private String dateOfBegin;

    @Column(name = "duration", nullable = false)
    private int durationOfContributionInYears;

    @Column(name = "sum_with_percent")
    private final int sumWithPercent = sumOfContribution + sumOfContribution * (percentByContribution / 100 * durationOfContributionInYears);

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    private Client contributor;
}
