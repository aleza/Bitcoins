package com.example.Bitcoins.services;

import com.example.Bitcoins.model.AdvicesCollection;
import com.example.Bitcoins.model.BuyBitArgNatBT;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

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

        List<AdvicesCollection> listAdvices = new ArrayList<>();
        //ArrayList<BuyBitArgNatBT> itemList = new ArrayList<>();
        buyBitArgNatBT = new BuyBitArgNatBT();
        for (int i = 0; i < productNode.get("data").get("ad_count").intValue(); i++) {

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

            listAdvices.add(advicesCollection);

            buyBitArgNatBT.setOccurs(productNode
                    .get("data")
                    .get("ad_count")
                    .intValue());
        }

        buyBitArgNatBT.setSalesAdvices(listAdvices);
        outputAdvices.writeToDirectory("/home/alexis/Bitcoins", "/home/alexis/Bitcoins/advices.txt", buyBitArgNatBT );

        return buyBitArgNatBT;
    }
}
