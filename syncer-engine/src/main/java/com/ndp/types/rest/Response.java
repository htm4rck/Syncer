package com.ndp.types.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private boolean successful;
    private Data<T> data;
    private String code;
    // Constructor parametrizado con @JsonCreator y @JsonProperty
    @JsonCreator
    public Response(@JsonProperty("successful") boolean successful,
                    @JsonProperty("data") Data<T> data) {
        this.successful = successful;
        this.data = data;
    }
}
