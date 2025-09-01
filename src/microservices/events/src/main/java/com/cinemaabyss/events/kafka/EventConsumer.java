package com.cinemaabyss.events.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "events-service-group")
    public void consume(String message) {
        log.info("[Consumer] Received: {}", message);
    }
}
