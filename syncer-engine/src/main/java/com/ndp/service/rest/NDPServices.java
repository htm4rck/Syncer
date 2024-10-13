package com.ndp.service.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.util.NDPEncryptPass;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class NDPServices {
    private static final Logger logger = Logger.getLogger(NDPServices.class);
    @Inject
    ObjectMapper objectMapper;
    @Inject
    NDPEncryptPass ndpEncryptPass;

    private String fingerprint = "45e0fcea3a3231f3ace7c83b616989ed";
    private String signature = "signature";
    private String identifier = "agarcia@360salesolutions.com";
    private String password = "Admin@9876";
    private String token = "";

    @Inject
    public NDPServices(NDPEncryptPass ndpEncryptPass) {
        this.ndpEncryptPass = ndpEncryptPass;
        this.token = login();
    }
    public String login() {
        try {
            assert this.ndpEncryptPass != null;
            this.signature = this.ndpEncryptPass.getEncryptedPassword(this.password, this.identifier);
            if (this.signature == null) {
                logger.error("Failed to get encrypted password");
                return null;
            }
            logger.warn("#######bien###############"+this.signature);
        } catch (Exception e) {
            logger.warn("###########cayo###########"+this.signature+e.getMessage());
        }
        try{
            String url = "https://azaleia.services.360salesolutions.com/secengine/auth/login";
            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json, text/plain, */*");
            headers.put("application", "app-posSystem-allowed");
            headers.put("content-type", "application/json");
            headers.put("fingerprint", this.fingerprint);
            headers.put("identifier", this.identifier);
            headers.put("signature", this.signature);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"));

            headers.forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                logger.warn("bodyResponse"+response.body());
                this.token = response.headers().firstValue("Token").orElse(null);
                logger.warn("Login request"+this.token);
                return this.token;
            } else {
                logger.error("Login request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during login", e);
            return null;
        }
    }

    public <T> List<T> ndpGet(String url, Class<T> clazz) {
        try {
            if (this.token == null || this.token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }

            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("company", "AZALEIA");
            headers.put("entity", "AZALEIA");
            headers.put("Token", this.token);
            headers.put("fingerprint", this.fingerprint);
            headers.put("signature", this.signature);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET();

            headers.forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                logger.warn("Response body: " + responseBody);
                //Root<T> responseObj = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructParametricType(Response.class, clazz));
                //logger.warn("Response object: " + responseObj);
                return null;
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

