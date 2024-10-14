package com.ndp.mapper.ndp.others;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditoryCoreDTO {
    private String ndpCase;
    private String companyCode;
    private String createDate;
    private String updateDate;
    private String status;
    private String code;
    private String id;
    private String codeERP;
}
