package com.example.Bitcoins.kafka;

import com.example.Bitcoins.model.AdvicesCollection;
import com.example.Bitcoins.model.BuyBitArgNatBT;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class AdvicesProducer {

    public void publishToKafka(BuyBitArgNatBT buyBitArgNatBT, String SERVER_HOME) {

        Properties properties = new Properties();

        //kafka bootstrap server
        properties.setProperty("bootstrap.servers", SERVER_HOME);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        //producer acks
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");

        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);

        List<AdvicesCollection> listAdvice = buyBitArgNatBT.getSalesAdvices();
        for (AdvicesCollection item : listAdvice) {

            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>(
                            "bitcoins_ARG_sales", item.getProfileUserName(),
                            "advice detail:" + item.getActionsPublicView()+"\n"+
                                    "location:" + item.getLocation()  +"\n"+
                                    "currency:" + item.getCurrency()  +"\n"+
                                    "min_amount:" + item.getMinAmount() +"\n"+
                                    "max_amount:" + item.getMaxAmount() +"\n"+
                                    "price:" + item.getTempPrice()
                    );
            producer.send(producerRecord);
        }
        producer.close();
    }
}
