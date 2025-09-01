package com.cinemaabyss.proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${MONOLITH_URL}")
    private String monolithUrl;

    @Value("${MOVIES_SERVICE_URL}")
    private String moviesUrl;

    @Value("${GRADUAL_MIGRATION:false}")
    private boolean gradualMigration;

    @Value("${MOVIES_MIGRATION_PERCENT:0}")
    private int migrationPercent;

    private final Random random = new Random();

    // --- Healthcheck ---
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\"}");
    }

    // --- Movies API ---
    @RequestMapping(
            value = "/movies/**",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                    RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS}
    )
    public ResponseEntity<?> handleMoviesRequests(RequestEntity<String> request) {
        String incomingPath = request.getUrl().getPath(); // /api/movies...

        // Новый сервис или монолит
        String targetUrl = shouldRouteToNewService()
                ? moviesUrl + incomingPath   // /api/movies → http://movies-service:8081/api/movies
                : monolithUrl + incomingPath; // → http://monolith:8080/api/movies

        return forwardRequest(targetUrl, request);
    }

    // --- Users API ---
    @RequestMapping(
            value = "/users/**",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
                    RequestMethod.DELETE, RequestMethod.HEAD, RequestMethod.OPTIONS}
    )
    public ResponseEntity<?> handleUsersRequests(RequestEntity<String> request) {
        String incomingPath = request.getUrl().getPath(); // /api/users...

        // users всегда идут в монолит
        String targetUrl = monolithUrl + incomingPath;

        return forwardRequest(targetUrl, request);
    }

    // --- Helpers ---
    private boolean shouldRouteToNewService() {
        if (!gradualMigration) {
            return false;
        }
        return random.nextInt(100) < migrationPercent;
    }

    private ResponseEntity<?> forwardRequest(String targetUrl, RequestEntity<String> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        HttpEntity<String> entity = new HttpEntity<>(request.getBody(), headers);

        try {
            return restTemplate.exchange(URI.create(targetUrl), request.getMethod(), entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Proxy error: " + e.getMessage() + "\"}");
        }
    }
}
