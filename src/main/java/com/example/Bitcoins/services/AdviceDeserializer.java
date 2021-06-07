package com.example.Bitcoins.services;

import com.example.Bitcoins.model.AdvicesCollection;
import com.example.Bitcoins.model.BuyBitArgNatBT;
import com.example.Bitcoins.model.ListAdvices;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@PropertySource("classpath:application.properties")
public class AdviceDeserializer extends StdDeserializer<BuyBitArgNatBT> {

    @Autowired
    private BuyBitArgNatBT buyBitArgNatBT;
    @Autowired
    private AdvicesCollection advicesCollection;
    @Autowired
    private OutputAdvices outputAdvices;


    public AdviceDeserializer() {
        this(null);
    }

    public AdviceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Value("${directory.home}")
    private String DIRECTORY_HOME_ADVICE;





    @Override
    public BuyBitArgNatBT deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {



        OutputAdvices outputAdvices = new OutputAdvices();
        JsonNode productNode = jp.getCodec().readTree(jp);


        ListAdvices listAdvices = new ListAdvices();
        ArrayList<BuyBitArgNatBT> itemList = new ArrayList<>();

        for (int i = 0; i < productNode.get("data").get("ad_count").intValue(); i++) {

            buyBitArgNatBT = new BuyBitArgNatBT();
            advicesCollection = new AdvicesCollection();

            advicesCollection.setProfileUserName(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("profile")
                    .get("username")
                    .textValue());

            advicesCollection.setLocation(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("location_string")
                    .textValue());

            advicesCollection.setCurrency(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("currency")
                    .textValue());

            advicesCollection.setMinAmount(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("min_amount")
                    .textValue());

            advicesCollection.setMaxAmount(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("max_amount")
                    .textValue());

            advicesCollection.setActionsPublicView(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("actions")
                    .get("public_view")
                    .textValue());

            buyBitArgNatBT.setSalesAdvices(advicesCollection);

            System.out.println("Advices list __________> " + buyBitArgNatBT.getSalesAdvices());

            //--------------------------------------------
            buyBitArgNatBT.setProfileUserName(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("profile")
                    .get("username")
                    .textValue());

            buyBitArgNatBT.setLocation(productNode
                    .get("data")
                    .get("ad_list")
                    .get(i)
                    .get("data")
                    .get("location_string")
                    .textValue());
            //---------------------------------

            buyBitArgNatBT.setOccurs(productNode
                    .get("data")
                    .get("ad_count")
                    .intValue());

            itemList.add(buyBitArgNatBT);

            System.out.println("Username --------------> " + buyBitArgNatBT.getProfileUserName());
            System.out.println("location_getLocation --> " + buyBitArgNatBT.getLocation());
            System.out.println("location_getOccurs ----> " + i);

        }
        listAdvices.setAdvice(itemList);

        outputAdvices.writeToDirectory("/home/alexis/Bitcoins", "/home/alexis/Bitcoins/advices.txt", listAdvices );


//        System.out.println("listAdvices  *****  " + listAdvices);

        return buyBitArgNatBT;
    }
}
