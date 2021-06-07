package com.example.Bitcoins.services;

import com.example.Bitcoins.model.CountryCodes;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class OutputCountryCodes {

    public void writeToDirectory(String directoryHome, String fileName, CountryCodes paises) throws IOException {

        try {
            File directory = new File(directoryHome); //Reviso si existe el directorio, sino lo creo
            if (!directory.exists())
                directory.mkdir();

            File ccodes = new File(fileName);

            if (ccodes.createNewFile()) {  //Reviso si existe el archivo, si existe lo borra y lo vuelve a crear
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
                ccodes.delete();
                ccodes.createNewFile();
            }

            FileWriter writer = new FileWriter(ccodes);
            JsonNode codigoPais = paises.getCodigos().get("cc_list");
            if (codigoPais.isArray()) {
                for (final JsonNode codigo : codigoPais) {
                    writer.write(String.valueOf(codigo + "\n"));
                }
            }
            writer.close();
        } catch (IOException ioException) {
            log.error("Se produjo un error al intentar grabar codigos de paises", ioException);
        }

    }
}
