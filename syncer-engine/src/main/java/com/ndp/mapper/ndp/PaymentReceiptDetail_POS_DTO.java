package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentReceiptDetail_POS_DTO extends AuditoryCoreDTO implements Serializable {

    private Integer numberLine;
    private Double unitPrice;
    private Double price;
    private Double quantity;
    private String unitMSRCode;
    private String unitMSRName;
    private String itemCode;
    private String itemName;
    private Double grossPrice;
    private Double grossPriceDiscount;
    private String taxCode;
    private String taxType;
    private Double tax;
    private Double subtotal;
    private Double subtotalDiscount;
    private Double discountPercentage;
    private Double discount;
    private Double total;
    private Double taxAmount;
    private Double bonuses;
    private String currency;
    //private Date updateDate;
    private String sunatOperation;
    private String sunatOneroso;
    private String sunatOperationType;
    private String sunatAffectationType;
    private String saleOrderCode;
    private Integer unitMSREntry;
    private String docReference;
    private Double quantityReturned;
    private Double quantitySaleOrder;
    private String warehouseCode;
    private String ubication;
    private Integer ubicationEntry;
    private String inventoryItem;
    private String onlyTax;
    private String saleOrderEntry;
    private Integer saleOrderLine;
    private Integer saleOrderDocNum;
    private Integer unitGroupEntry;
    private Double unitPriceReal;
    private Double baseQuantity;
    private Double quantitySentReferralGuide;
    private Double unitWeight;
    private Double totalWeight;
    private String brandCode;
    private String brandName;
    private String familyCode;
    private String familyName;
    private String ubicationTo;
    private String costingCode;
    private String costingCode2;
    private String costingCode3;
    private String costingCode4;
    private String costingCode5;
    private String isGrossPrice;
    private String dataTax;
}
