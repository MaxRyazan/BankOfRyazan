package ru.maxryazan.bankofryazan.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "metal_rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "silver")
    @JsonProperty("XAG")
    private float silver;

    @Column(name = "gold")
    @JsonProperty("XAU")
    private float gold;

    @Column(name = "palladium")
    @JsonProperty("XPD")
    private float palladium;

    @Column(name = "platinum")
    @JsonProperty("XPT")
    private float platinum;

    @Column(name = "rhodium")
    @JsonProperty("XRH")
    private float rhodium;

    @Column(name = "date")
    private String date;


}
