package ru.maxryazan.bankofryazan.models.accient_rate_api_response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDto {

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "patronymic")
    private String patronymic;

    @JsonProperty(value = "accidentRate")
    private double accidentRate;

    @JsonProperty(value = "discount")
    private int discount;

    public ClientDto(double accidentRate, int discount) {
        this.accidentRate = accidentRate;
        this.discount = discount;
    }
}
