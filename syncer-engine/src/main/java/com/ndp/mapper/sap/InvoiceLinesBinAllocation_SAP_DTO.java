package com.ndp.mapper.sap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class InvoiceLinesBinAllocation_SAP_DTO implements Serializable {

    private Integer BinAbsEntry;
    private Double Quantity;

}