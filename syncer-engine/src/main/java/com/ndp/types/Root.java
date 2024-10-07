// Root.java
package com.ndp.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Root<T> {
    private boolean successful;
    private String code;
    private String message;
    private List<T> data;
}