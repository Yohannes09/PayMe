package com.payme.emailService.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConfig {

    // Responsible for creating Kafka producers.
    @Bean
    public ProducerFactory<String, String> producerFactory(){
        // Use map to define the producer's settings.
        Map<String, Object> configProperties = new HashMap<>();

        // Tells Kafka broker(s) that the producer to connects to localhost:9092
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Tells Kafka how to serialize the key and value.
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProperties);
    }

    // Acts as the API for sending messages to Kafka.
    @Bean
    public KafkaTemplate kafkaTemplate(){
        return new KafkaTemplate(producerFactory());
    }

}
