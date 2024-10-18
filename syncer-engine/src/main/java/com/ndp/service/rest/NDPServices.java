package com.ndp.service.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.types.rest.Response;
import com.ndp.util.Formatters;
import com.ndp.util.NDPEncryptPass;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.ndp.entity.syncer.Business;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class NDPServices {
    private static final Logger logger = Logger.getLogger(NDPServices.class);
    @Inject
    ObjectMapper objectMapper;
    @Inject
    NDPEncryptPass ndpEncryptPass;
    @Inject
    Formatters formatters;
    private final String path;
    private final String fingerprint = "45e0fcea3a3231f3ace7c83b616989ed";
    private String signature = "signature";
    private final String identifier;
    private final String password;
    private final String company;
    private String token;

    @Inject
    public NDPServices(Business business) {
        this.formatters = new Formatters();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.ndpEncryptPass = new NDPEncryptPass();
        this.path = business.getPath();
        this.identifier = business.getUser();
        this.password = business.getPass();
        this.company = business.getCompany();
        this.token = login();
    }

    public String login() {
        try {
            assert this.ndpEncryptPass != null;
            this.signature = this.ndpEncryptPass.getEncryptedPassword(this.password, this.identifier);
            String url = this.path + "secengine/auth/login";
            logger.warn("URL Servicio NDP: " + this.path);
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
                this.token = response.headers().firstValue("Token").orElse(null);
                logger.warn("Token Servicio NDP: " + this.token);
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

    public <T> Response<T> ndpGet(String url, Class<T> clazz) {
        try {
            if (this.token == null || this.token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }

            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("company", this.company);
            headers.put("entity", this.company);
            headers.put("Token", this.token);
            headers.put("fingerprint", this.fingerprint);
            headers.put("signature", this.signature);

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(this.path + url))
                    .GET();

            headers.forEach(requestBuilder::header);
            logger.warn(formatters.getLog(
                    clazz.getName(),
                    "REQUEST",
                    "null",
                    "GET NDP: " + clazz.getName()
            ));

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                logger.warn(formatters.getLog(
                        clazz.getName(),
                        "RESPONSE",
                        "GET NDP: " + clazz.getName(),
                        responseBody

                ));
                logger.warn("Response body: " + responseBody);
                logger.warn("Response Clazz: " + clazz.getName());
                return objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructParametricType(Response.class, clazz));
            } else {
                logger.error("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during sequence data request", e);
            return null;
        }
    }

    public <T> T ndpPost(String url, Object requestObject, Class<T> responseClass) {
        try {
            if (this.token == null || this.token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            String requestBody = objectMapper.writeValueAsString(requestObject);
            logger.warn("Request Body " + requestBody);
            logger.warn("Request URL Post" + this.path + url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.path + url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + this.token)
                    .header("company", this.company)
                    .header("entity", this.company)
                    .header("Token", this.token)
                    .header("fingerprint", this.fingerprint)
                    .header("signature", this.signature)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.warn("ResponseStatus: " + response.statusCode());
            logger.warn("Response Body: " + response.body());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), responseClass);
            } else {
                logger.error("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during POST request", e);
            return null;
        }
    }

    public <T> T ndpPatch(String url, Object requestObject, Class<T> responseClass) {
        try {
            if (this.token == null || this.token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
            String requestBody = objectMapper.writeValueAsString(requestObject);
            logger.warn("Request patch Body " + requestBody);
            logger.warn("Request patch URL" + this.path + url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.path + url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + this.token)
                    .header("company", this.company)
                    .header("entity", this.company)
                    .header("Token", this.token)
                    .header("fingerprint", this.fingerprint)
                    .header("signature", this.signature)
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.warn("ResponseStatus: " + response.statusCode());
            logger.warn("Response Body: " + response.body());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), responseClass);
            } else {
                logger.error("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error during POST request", e);
            return null;
        }
    }
}

