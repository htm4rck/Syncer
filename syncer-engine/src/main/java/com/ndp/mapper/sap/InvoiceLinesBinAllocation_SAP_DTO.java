package com.ndp.mapper.sap;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class InvoiceLinesBinAllocation_SAP_DTO implements Serializable {

    private Integer BinAbsEntry;
    private Double Quantity;

}
