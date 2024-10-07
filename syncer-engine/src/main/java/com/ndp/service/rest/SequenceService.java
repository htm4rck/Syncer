// SequenceService.java
package com.ndp.service.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.types.Root;
import com.ndp.types.Sequence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SequenceService {

    private static final Logger logger = Logger.getLogger(SequenceService.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    SecurityService securityService;

    public Root<Sequence> getSequenceData() {
        String jsonData = fetchJsonData();
        if (jsonData == null || jsonData.isEmpty()) {
            // Log the issue and return null or throw an exception
            System.err.println("No JSON data to deserialize");
            return null;
        }
        try {
            return objectMapper.readValue(jsonData, new TypeReference<Root<Sequence>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchJsonData() {

        String token = securityService.login();
        String signature = securityService.signature;
        String fingerprint = securityService.fingerprint;
        try {
            if (token == null || token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }

            String url = "https://azaleia.services.360salesolutions.com/process-operation/sequence";
            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("company", "AZALEIA");
            headers.put("entity", "AZALEIA");
            headers.put("Token", token);
            headers.put("fingerprint", fingerprint);
            headers.put("signature", signature);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();

            headers.forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                logger.error("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during sequence data request", e);
            return null;
        }
    }
}
