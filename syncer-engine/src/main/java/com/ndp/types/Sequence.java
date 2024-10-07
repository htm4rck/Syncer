package com.ndp.types;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sequence {
    private String id;
    private String processType;
    private String objectId;
    private String parentEntityId;
    private String objectName;
    private String serviceAddress;
    private String path;
    private String channelOrigin;
    private String storeId;
    private String companyId;
    private String successData;
    private String errorData;
    private String data;
    private String status;
    private String objectCreationDate;
    private DataIfPresent dataIfPresent;
    private boolean pending;

    // Getters and Setters
}
