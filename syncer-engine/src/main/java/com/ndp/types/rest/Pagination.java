package com.ndp.types.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pagination<T> {
    private int countFilter;
    private int totalPages;
    private List<T> list;

    @JsonCreator
    public Pagination(@JsonProperty("countFilter") int countFilter,
                      @JsonProperty("totalPages") int totalPages,
                      @JsonProperty("list") List<T> list) {
        this.countFilter = countFilter;
        this.totalPages = totalPages;
        this.list = list;
    }
}
