package com.ndp.types.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Data<T> {
    private Pagination<T> pagination;

    @JsonCreator
    public Data(@JsonProperty("pagination") Pagination<T> pagination) {
        this.pagination = pagination;
    }
}