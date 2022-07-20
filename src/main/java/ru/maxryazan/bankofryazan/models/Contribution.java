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
@Table(name = "contribution")
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number_of_contribution", nullable = false)
    private String numberOfContribution;

    @Column(name = "sum", nullable = false)
    private double sumOfContribution;

    @Column(name = "percent", nullable = false)
    private double percentByContribution;

    @Column(name = "date_of_begin", nullable = false)
    private String dateOfBegin;

    @Column(name = "date_of_end", nullable = false)
    private String dateOfEnd;

    @Column(name = "duration", nullable = false)
    private int durationOfContributionInYears;

    @Column(name = "sum_with_percent")
    private double sumWithPercent;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "contributor_id")
    private Client contributor;
}
