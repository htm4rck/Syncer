package com.ndp.mapper.sap;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class DocumentInstallments_SAP_NDP_DTO {

    private String DueDate;
    private Double Percentage;
    private String U_VS_TIPOCUOTA;
    private String U_VS_TIPOSERVICIO;

}
