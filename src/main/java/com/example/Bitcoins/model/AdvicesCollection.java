package com.example.Bitcoins.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AdvicesCollection {
    private String profileUserName;
    private String location;
    private String currency;
    private String minAmount;
    private String maxAmount;
    private String actionsPublicView;
}
