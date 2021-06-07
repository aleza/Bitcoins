package com.example.Bitcoins.services;

import com.example.Bitcoins.model.BuyBitArgNatBT;
import com.example.Bitcoins.model.ListAdvices;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class OutputAdvices {

    @Value("${directory.home}")
    private String DIRECTORY_HOME_ADVICE;


    public void writeToDirectory(String directoryHome, String fileName, ListAdvices listAdvices) throws IOException {

        try {
            File directory = new File(directoryHome); //Reviso si existe el directorio, sino lo creo
            if (!directory.exists())
                directory.mkdir();

            File file_advices = new File(fileName);

            if (file_advices.createNewFile()) {  //Reviso si existe el archivo, si existe lo borra y lo vuelve a crear
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
                file_advices.delete();
                file_advices.createNewFile();
            }

            FileWriter writer = new FileWriter(file_advices);


            List<BuyBitArgNatBT> listAdvice = listAdvices.getAdvice();

            for (BuyBitArgNatBT item : listAdvice) {
                writer.write(String.valueOf(item)+"\n");
            }


            writer.close();
        } catch (IOException ioException) {
            log.error("Se produjo un error al intentar grabar los avisos", ioException);
        }

    }
}
