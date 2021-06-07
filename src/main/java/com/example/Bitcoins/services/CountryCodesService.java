package com.example.Bitcoins.services;

import com.example.Bitcoins.model.BuyBitArgNatBT;
import com.example.Bitcoins.model.CountryCodes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Component
@EnableScheduling
@Slf4j
public class CountryCodesService {

    @Autowired
    private OutputCountryCodes outputCountryCodes;

    @Autowired
    private OutputAdvices outputAdvices;

    public CountryCodesService() {
        FILE_COUNTRY_CODES = null;
        DIRECTORY_HOME = null;
        BUY_ARG_NAT_BANK_TRANSFER = null;
        FILE_ADVICES = null;
    }


    @Value("${directory.home}")
    private final String DIRECTORY_HOME;
    @Value("${file.country-codes}")
    private final String FILE_COUNTRY_CODES;
    @Value("${file.advices}")
    private final String FILE_ADVICES;
    @Value("${url.buy-arg-nat-bank-transfer}")
    private final String BUY_ARG_NAT_BANK_TRANSFER;

    /*
    @Scheduled(fixedRateString = "${app.schedule.rate-ms}")
    public void execute() {

        try {

            CountryCodes paises = WebClient.create()
                    .get()
                    .uri("https://localbitcoins.com//api/countrycodes/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve() // obtiene la respuesta
                    .bodyToMono(com.example.Bitcoins.model.CountryCodes.class)
                    .block();

            outputCountryCodes.writeToDirectory(DIRECTORY_HOME, FILE_COUNTRY_CODES, paises);

        } catch (WebClientResponseException | IOException responseException) {
            log.error("Se produjo un error en la consulta HTTP", responseException);
        }
    }
     */

    @Scheduled(fixedRateString = "${app.schedule.rate-ms}")
    public void execute() {

        try {

            String bitcoinsOffers = WebClient.create()
                    .get()
                    .uri(BUY_ARG_NAT_BANK_TRANSFER)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve() // obtiene la respuesta
                    .bodyToMono(String.class)
                    .block();

            //outputCountryCodes.writeToDirectory(DIRECTORY_HOME, FILE_COUNTRY_CODES, paises);

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();

            module.addDeserializer(BuyBitArgNatBT.class, new AdviceDeserializer());
            mapper.registerModule(module);
            BuyBitArgNatBT advice = mapper.readValue(bitcoinsOffers, BuyBitArgNatBT.class);

            for (int i = 0; i < advice.getOccurs(); i++) {
                System.out.println("Username --------------> " + advice.getProfileUserName());
                System.out.println("location_getLocation --> " + advice.getLocation());
                System.out.println("location_getOccurs ----> " + advice.getOccurs());

               // System.out.println("Advices list __________> " + advice.getSalesAdvices());
            }

//            outputAdvices.writeToDirectory(DIRECTORY_HOME, FILE_ADVICES);

        } catch (WebClientResponseException responseException) {
            log.error("Se produjo un error en la consulta HTTP", responseException);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}
