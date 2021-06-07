package com.example.Bitcoins.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;


@Data
public class CountryCodes {

    @JsonProperty("data")
    private JsonNode codigos;


}
