package com.ndp.service.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SAPServices {
    private static final Logger logger = Logger.getLogger(SAPServices.class);
    private String URLBase = "https://azaleia.sl.360salesolutions.com/b1s/v1/";
    private Optional<String> cookie = Optional.empty();

    public Optional<String> loginToSAP() {
        try {
            String url = URLBase + "Login";
            String json = """
                {
                    "CompanyDB": "B1H_AZALEIA_LOCALIZACION2",
                    "Password": "B1Admin$$",
                    "UserName": "manager"
                }
            """;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<String> cookies = response.headers().allValues("Set-Cookie");
                if (!cookies.isEmpty()) {
                    cookie = Optional.of(cookies.get(0));
                    return cookie;
                }
            } else {
                logger.error("Login request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error during login to SAP", e);
        }
        return Optional.empty();
    }

    public Optional<String> getCookie() {
        return cookie;
    }

    public <T> Optional<String> sendPostRequest(String path, String cookie, T object) {
        try {
            String url = URLBase + path;
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(object);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Cookie", cookie)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return Optional.of(response.body());
            } else {
                logger.error("POST request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.error("Error during POST request to SAP", e);
        }
        return Optional.empty();
    }
}