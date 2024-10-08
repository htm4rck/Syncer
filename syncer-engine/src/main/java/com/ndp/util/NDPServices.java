package com.ndp.util;

import com.ndp.service.rest.SecurityService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class NDPServices {
    private static final Logger logger = Logger.getLogger(SecurityService.class);

    @Inject
    SecurityService securityService;
    public String getObject(String url) {

        String token = securityService.login();
        String signature = securityService.signature;
        String fingerprint = securityService.fingerprint;
        try {
            if (token == null || token.isEmpty()) {
                logger.error("Token is not available. Please login first.");
                return null;
            }

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
