package com.ndp.types.rest;

import com.ndp.types.rest.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private boolean successful;
    private Data<T> data;
    public Response(boolean successful, Data<T> data) {
        this.successful = successful;
        this.data = data;
    }
}

