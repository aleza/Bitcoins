package com.example.Bitcoins.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Data
public class BuyBitArgNatBT {
    private String profileUserName;
    private String location;
    private String currency;
    private BigInteger minAmount;
    private BigInteger maxAmount;
    private String actionsPublicView;
    private int occurs;
    private AdvicesCollection salesAdvices;
 }
