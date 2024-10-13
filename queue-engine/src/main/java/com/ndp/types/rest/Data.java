package com.ndp.types.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data<T> {
    private Pagination<T> pagination;

    public Data(Pagination<T> pagination) {
        this.pagination = pagination;
    }
}
