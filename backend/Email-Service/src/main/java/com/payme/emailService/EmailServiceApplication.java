package com.payme.emailService;

import com.payme.emailService.service.KafkaProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailServiceApplication implements CommandLineRunner {

	private final KafkaProducer kafkaProducer;

	public EmailServiceApplication(KafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	public static void main(String[] args) {
		SpringApplication.run(EmailServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String event = String.format("""
        {
            "eventType": "UserRegistered",
            "email": "%s"
        }
        """, "example@email.com");
		kafkaProducer.sendEvent("user-events", event);
	}
}
