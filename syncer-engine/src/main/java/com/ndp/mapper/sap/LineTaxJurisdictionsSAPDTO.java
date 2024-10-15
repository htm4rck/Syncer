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
public class LineTaxJurisdictionsSAPDTO implements Serializable {
    private String JurisdictionCode; // TODO Codigo de Impuesto: I18
    private Integer JurisdictionType;// TODO Tipo de Documento Ventas: 8
    private Double ExternalCalcTaxRate; // TODO Porcentaje de Impuesto 18.0
    private Double ExternalCalcTaxAmount; // TODO Monto de Impuesto a 2 decimanles x.yz
}
