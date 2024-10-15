package com.ndp.mapper.sap;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class DocumentInstallments_SAP_NDP_DTO {

    private String DueDate;
    private Double Percentage;
    private String U_VS_TIPOCUOTA;
    private String U_VS_TIPOSERVICIO;

}
