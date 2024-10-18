package com.ndp.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueDto {
    private String method;
    private String uid;
    private String code;
    private String document;
    private String object;
    private String path;
    private String storeCode;
    private String company;
    private String createDate;
    private String status;
    private Integer attempts;


}
