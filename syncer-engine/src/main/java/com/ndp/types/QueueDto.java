package com.ndp.types;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class QueueDto {
    private String method;
    private String uid;
    private String code;
    private String document;
    private String object;
    private String path;
    private String storeCode;
    private String company;
    private LocalDateTime createDate;
    private String status;
    private Integer attempts;
}
