package ru.maxryazan.bankofryazan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Getter
@Setter
public class MetalRate {

    @JsonIgnore
    @Transient
    private String date;

    @JsonProperty("rates")
    @OneToOne
    private Rate rates;

    @JsonIgnore
    private boolean success;

    @JsonIgnore
    private long timestamp;

    @JsonIgnore
    private String base;

    @JsonIgnore
    private String unit;


}
