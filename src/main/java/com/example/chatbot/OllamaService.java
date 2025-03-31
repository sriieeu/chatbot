package com.example.chatbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class OllamaService {

    private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "gemma3:1b";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getOllamaResponse(String message) {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();              //header
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        //  request-body 
        OllamaRequest request = new OllamaRequest(
            MODEL_NAME, 
            message,                   
            100, 0.7
        );

        
        
        HttpEntity<OllamaRequest> entity = new HttpEntity<>(request, headers);     //request 
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(OLLAMA_API_URL, entity, String.class);
            String responseBody = response.getBody();
          
            if (responseBody == null) {
                return " No response from Ollama API.";
            }

            // Handle multi-line JSON responses
            StringBuilder fullResponse = new StringBuilder();
            String[] lines = responseBody.split("\n");
            
            for (String line : lines) {
                JsonNode jsonNode = objectMapper.readTree(line);
                if (jsonNode.has("response")) {
                    fullResponse.append(jsonNode.get("response").asText()).append(" ");
                }
            }

            String finalResponse = fullResponse.toString().trim();
            return finalResponse.isEmpty() ? " No 'response' field in Ollama API response." : finalResponse;

        } catch (Exception e) {
            return " Error: Unable to connect to Ollama API. " + e.getMessage();
        }
    }
}
