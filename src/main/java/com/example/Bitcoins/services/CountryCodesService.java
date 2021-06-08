package com.example.Bitcoins.services;

import com.example.Bitcoins.model.BuyBitArgNatBT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

@Component
@EnableScheduling
@Slf4j
public class CountryCodesService {

    @Autowired
    private OutputCountryCodes outputCountryCodes;

    @Autowired
    private OutputAdvices outputAdvices;

    @Value("${directory.home}")
    private String DIRECTORY_HOME;
    @Value("${file.country-codes}")
    private String FILE_COUNTRY_CODES;
    @Value("${file.advices}")
    private String FILE_ADVICES;
    @Value("${url.buy-arg-nat-bank-transfer}")
    private String BUY_ARG_NAT_BANK_TRANSFER;

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

            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();

            module.addDeserializer(BuyBitArgNatBT.class, new AdviceDeserializer(DIRECTORY_HOME, FILE_ADVICES));
            mapper.registerModule(module);
            BuyBitArgNatBT advice = mapper.readValue(bitcoinsOffers, BuyBitArgNatBT.class);


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
