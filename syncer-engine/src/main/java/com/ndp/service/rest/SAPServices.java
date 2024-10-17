package com.ndp.service.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.entity.syncer.Business;
import com.ndp.util.Formatters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class SAPServices {
    private static final Logger logger = Logger.getLogger(SAPServices.class);
    private String URLBase;
    private String companyBD;
    private String password;
    private String userName;
    private Optional<String> cookie = Optional.empty();
    @Inject
    Formatters formatters;

    @Inject
    public SAPServices(Business business) {
        if (business == null || business.getPath() == null) {
            throw new IllegalArgumentException("Business object or URLBase cannot be null");
        }
        this.formatters = new Formatters();
        this.URLBase = business.getPath();
        this.companyBD = business.getCompanyBD();
        this.userName = business.getUser();
        this.password = business.getPass();
        loginToSAP();
    }

    public Optional<String> loginToSAP() {
        try {
            // Ensure URLBase contains a valid URL with scheme
            if (!URLBase.startsWith("http://") && !URLBase.startsWith("https://")) {
                throw new IllegalArgumentException("URLBase must start with http:// or https://");
            }

            String url = URLBase + "Login";
            logger.warn("URL: " + url);
            String json = String.format("""
                        {
                            "CompanyDB": "%s",
                            "Password": "%s",
                            "UserName": "%s"
                        }
                    """, companyBD, password, userName);
            logger.warn("JSON login SAP: " + json);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.warn("ResponseStatusSAP: " + response.statusCode());
            logger.warn("ResponseSAP: " + response.body());
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

    public <T> Optional<String> sendPostRequest(String path, T object) {
        try {
            String url = URLBase + path;
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(object);
            logger.warn(formatters.getLog(
                    "Clase",
                    "REQUEST",
                    "POST SAP: " + "Clase",
                    json

            ));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Cookie", cookie.get())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.warn("ResponseStatusSAP: " + response.statusCode());
            logger.warn("ResponseSAP: " + response.body());
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