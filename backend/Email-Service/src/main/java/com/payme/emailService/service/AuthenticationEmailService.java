package com.payme.emailService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationEmailService {

    private final ObjectMapper objectMapper;

    public AuthenticationEmailService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "user-events", groupId = "auth-email-service")
    public void consumeEvent(ConsumerRecord<String, String> record){
        try {
            String message = record.value();
            System.out.println(message);

            JsonNode event = objectMapper.readTree(message);
            String eventType = event.get("eventType").asText();
            System.out.println(eventType);

            processEvent(eventType, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processEvent(String eventType, JsonNode event) {
        switch (eventType){
            case "user-registered" -> handleUserRegistrationEmail();
            case "password-reset" -> handleUserPasswordReset();
        }
    }

    private void handleUserRegistrationEmail() {
        log.info("handleUserRegistrationEmail()");
    }

    private void handleUserPasswordReset() {
        log.info("handleUserPasswordReset()");
    }

}
