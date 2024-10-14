package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndp.mapper.ndp.others.AuditoryCoreDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditNoteDetailDTO extends AuditoryCoreDTO implements Serializable {

    private String creditNoteCode;
    private Integer numberLine;
    private Double quantity;
    private String unitMSRCode;
    private String unitMSRName;
    private String itemCode;
    private Integer ubicationEntry;
    private String itemName;
    private String warehouseCode;
    private String ubicationCode;
    private Double unitPrice;
    private Double grossPrice;
    private String taxCode;
    private Double tax;
    private Double subtotal;
    private Double subtotalWithDiscount;
    private Double discountPercentage;
    private Double discount;
    private Double total;
    private Integer sample;
    private Double dispatchDisponible;
    private Double quantityInvoiced;
    private String updateDate;
    private String sunatOperation;
    private String sunatOneroso;
    private String sunatOperationType;
    private String sunatAffectationType;
    private String currency;
    private String createUser;
    private Integer unitMSREntry;
    private Double quantityPicking;
    private Double taxAmount;
    private String taxType;
    private String inventoryItem;
    private String onlyTax;
    private Integer unitGroupEntry;
    private Double unitPriceReal;
    private Double baseQuantity;
    private Double unitWeight;
    private Double totalWeight;
    private String costingCode;
    private String costingCode2;
    private String costingCode3;
    private String costingCode4;
    private String costingCode5;

    private Double subtotalWithIgv;
    private Double discountWithIgv;
    private Double grossPriceDiscount;


    private Double porDet;

    /**
     * Field to indicate if the original price includes IGV: "Y" or "N"
     */

    private String isGrossPrice;

    /**
     * Field to indicate if inventory is not going to be moved or not,
     * for reserve invoice without deliveries: "tYES", otherwise: "tNO"
     */

    private String withoutInventoryMovement;

    /**
     * Field to store the type of base voucher,
     * as long as it is not paid, for reservation invoice it is 13
     */

    private Integer baseType;  //13

    /**
     * Field to store the line number of the
     * origin detail, in this case the detail of the voucher
     */

    private Integer baseLine;

    /**
     * Field to store the DocEntry of the base
     * document (known in 360 as codeERP) in this case of the receipt
     */

    private String baseEntry; // codeErp of base document

    private String accountCode; //
    private String isUnitCost;
    private Double unitCost;
    private String dataTax;
}
