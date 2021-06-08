package com.example.Bitcoins.services;

import com.example.Bitcoins.kafka.AdvicesProducer;
import com.example.Bitcoins.model.AdvicesCollection;
import com.example.Bitcoins.model.BuyBitArgNatBT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class OutputAdvices {

    public void writeToDirectory(String directoryHome, String fileName, BuyBitArgNatBT buyBitArgNatBT) throws IOException {

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

            List<AdvicesCollection> listAdvice = buyBitArgNatBT.getSalesAdvices();

            for (AdvicesCollection item : listAdvice) {
                writer.write(String.valueOf(
                        "user="+item.getProfileUserName())+"\n"+
                        "advice="+item.getActionsPublicView()+"\n"+
                        "location="+item.getLocation()+"\n"+
                        "currency="+item.getCurrency()+"\n"+
                        "min_amount="+item.getMinAmount()+"\n"+
                        "max_amount="+item.getMaxAmount()+"\n"+
                        "temp_price="+item.getTempPrice()+"\n");
            }

            writer.close();

            AdvicesProducer advicesProducer = new AdvicesProducer();
            advicesProducer.publishToKafka(buyBitArgNatBT);

        } catch (IOException ioException) {
            log.error("Se produjo un error al intentar grabar los avisos", ioException);
        }

    }
}
