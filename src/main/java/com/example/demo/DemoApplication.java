package com.example.demo;

import ch.qos.logback.core.spi.LogbackLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Autowired
	KafkaTemplate<String,String> kafkaTemplate;

	@Override
	public void run(String... args) throws Exception {
		String message = "hello " + ThreadLocalRandom.current().nextLong();
		log.info("send message {}", message);
		ProducerRecord<String,String> pr = new ProducerRecord<String,String>("topic", message);
		CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(pr);
		SendResult<String, String> result = send.join();
		System.out.println(result.getRecordMetadata());
	}

	@KafkaListener(id = "a new id 3", topics = "topic")
	public void listen(String in) {
		log.info("got message {}", in);
	}


}
