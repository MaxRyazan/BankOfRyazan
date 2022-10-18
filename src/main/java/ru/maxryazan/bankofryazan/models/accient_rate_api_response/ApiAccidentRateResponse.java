package ru.maxryazan.bankofryazan.models.accient_rate_api_response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiAccidentRateResponse {

    @JsonProperty(namespace = "message")
    private ClientDto message;

    @JsonProperty(namespace = "responseCode")
    private String responseCode;
}
