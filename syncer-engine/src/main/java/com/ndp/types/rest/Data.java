package com.ndp.types.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data<T> {
    private Pagination<T> pagination;
    @JsonProperty("count")
    private int count;

    @JsonProperty("value")
    private List<T> value;
    @JsonCreator
    public Data(@JsonProperty("pagination") Pagination<T> pagination) {
        this.pagination = pagination;
    }
}