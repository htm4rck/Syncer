package com.ndp.service.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SecurityService {
    public String fingerprint = "45e0fcea3a3231f3ace7c83b616989ed";
    public String signature = "signature";
    public String identifier = "agarcia@360salesolutions.com";
    public String password = "Admin@9876";
    public String token = "";

    private static final Logger logger = Logger.getLogger(SecurityService.class);

    public String getEncryptedPassword(String password, String email) {
        try {
            String url = String.format("https://azaleia.services.360salesolutions.com/security-engine/util/signature?password=%s&email=%s", password, email);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                logger.error("Request failed with status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error while fetching encrypted password", e);
            return null;
        }
    }

    public String login() {
        try {
            this.signature = getEncryptedPassword(this.password, this.identifier);
            if (this.signature == null) {
                logger.error("Failed to get encrypted password");
                return null;
            }

            String url = "https://azaleia.services.360salesolutions.com/secengine/auth/login";
            HttpClient client = HttpClient.newHttpClient();
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json, text/plain, */*");
            headers.put("accept-language", "es-419,es;q=0.9,es-ES;q=0.8,en;q=0.7,en-GB;q=0.6,en-US;q=0.5,es-PE;q=0.4");
            headers.put("application", "app-posSystem-allowed");
            headers.put("content-type", "application/json");
            headers.put("fingerprint", this.fingerprint);
            headers.put("identifier", this.identifier);
            headers.put("origin", "https://pos.360salesolutions.com");
            headers.put("priority", "u=1, i");
            headers.put("referer", "https://pos.360salesolutions.com/");
            headers.put("sec-ch-ua", "\"Microsoft Edge\";v=\"129\", \"Not=A?Brand\";v=\"8\", \"Chromium\";v=\"129\"");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "\"Windows\"");
            headers.put("sec-fetch-dest", "empty");
            headers.put("sec-fetch-mode", "cors");
            headers.put("sec-fetch-site", "same-site");
            headers.put("signature", this.signature);
            headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0");

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString("{}"));

            headers.forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                this.token = response.headers().firstValue("Token").orElse(null);
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
}