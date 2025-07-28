package com.portfolio.controller;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gemini-proxy")
@CrossOrigin(origins = "https://profile2-murex.vercel.app/")
public class GeminiController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate =new RestTemplate();

    @PostMapping
    public ResponseEntity<String> proxyRequest(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        String mode = payload.get("mode");
        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("The 'prompt' field cannot be empty.");
        }

        if (geminiApiKey == null || geminiApiKey.isEmpty() || "YOUR_GEMINI_API_KEY_HERE".equals(geminiApiKey)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API Key is not configured on the server.");
        }

        // The official Gemini API endpoint
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        Map<String, Object> generationConfig = Map.of(
                "responseMimeType", "application/json",
                "responseSchema", getResponseSchemaForMode(mode)
        );

        Map<String, Object> requestBodyMap = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
                "generationConfig", generationConfig
        );


        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBodyMap, headers);

        try {
            // Forward the request to the Gemini API
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.err.println("Error while calling Gemini API: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while contacting the Gemini API.");
        }
    }

    private Map<String, Object> getResponseSchemaForMode(String mode) {
        if ("ideas".equals(mode)) {
            return Map.of(
                    "type", "OBJECT",
                    "properties", Map.of(
                            "projectName", Map.of("type", "STRING"),
                            "description", Map.of("type", "STRING"),
                            "features", Map.of("type", "ARRAY", "items", Map.of("type", "STRING"))
                    )
            );
        }
        // Default to 'recommend' mode schema
        return Map.of(
                "type", "OBJECT",
                "properties", Map.of(
                        "projectType", Map.of("type", "STRING"),
                        "summary", Map.of("type", "STRING"),
                        "stack", Map.of(
                                "type", "OBJECT",
                                "properties", Map.of(
                                        "Frontend", Map.of("type", "ARRAY", "items", Map.of("type", "STRING")),
                                        "Backend", Map.of("type", "ARRAY", "items", Map.of("type", "STRING")),
                                        "Database", Map.of("type", "ARRAY", "items", Map.of("type", "STRING")),
                                        "Deployment", Map.of("type", "ARRAY", "items", Map.of("type", "STRING"))
                                )
                        )
                )
        );
    }
}
