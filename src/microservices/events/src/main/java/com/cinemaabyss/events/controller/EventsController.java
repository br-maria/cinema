package com.cinemaabyss.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventsController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of("status", true)); // ✅ тест ждёт boolean
    }

    @PostMapping("/movie")
    public ResponseEntity<Map<String, String>> createMovieEvent(@RequestBody Map<String, Object> payload) {
        log.info("Received movie event: {}", payload);
        kafkaTemplate.send("movies", payload.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success")); // ✅ 201 + success
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> createUserEvent(@RequestBody Map<String, Object> payload) {
        log.info("Received user event: {}", payload);
        kafkaTemplate.send("users", payload.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success"));
    }

    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> createPaymentEvent(@RequestBody Map<String, Object> payload) {
        log.info("Received payment event: {}", payload);
        kafkaTemplate.send("payments", payload.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "success"));
    }
}
