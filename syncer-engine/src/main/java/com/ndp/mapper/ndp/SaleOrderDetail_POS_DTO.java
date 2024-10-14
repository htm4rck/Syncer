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
public class SaleOrderDetail_POS_DTO extends AuditoryCoreDTO implements Serializable {

    private String orderSaleCode;
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
    private Double price;
    private Double grossPrice;
    private Double grossPriceDiscount;
    private String taxCode;
    private String taxType;
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
    private String inventoryItem;
    private String onlyTax;
    private Integer unitGroupEntry;
    private String costingCode;
    private String costingCode2;
    private String costingCode3;
    private String costingCode4;
    private String costingCode5;
    private String projectCode;
    private String masterBoxCode;
    private Double amountMasterBox;
    private String alternativeItemName;
    private String alternativeItemCode;
    private String itemType;
    private Double baseQuantity;
    private String type;
    private String afeDet;
    private String codSer;
    private Double porDet;
    private Double unitWeight;
    private Double totalWeight;
    private String brandCode;
    private String brandName;
    private String familyCode;
    private String familyName;
    private String ubicationTo;
    private String warehouseTo;
    private Integer ubicationEntryTo;
    private Double subtotalWithIgv;
    private String isGrossPrice;
    private Double unitPriceBD;
    private String offerCode;
    private String offerApply;
    private String glosa;
    private String warehouseName;
    private String masterBoxName;
    private String projectName;
    private Double discount1;
    private Double discount2;
    private Double discount3;
    private Double discount4;
    /* Aditional Data */
    private String  itemModel;
    private String  foil;

    private String  drainage;
    private String  packaged;
    private String  sticker;
    private String  foodContact;
    private String  customPackaging;
    private String  barcode;

    private String  productOrigin;
    private String  accreditationCal;
    private String  accreditationDino;
    private String  promotional;
    private String  serigrafiar;
    private String  sampleApproved;
    private String isMiscellaneous;
    private String modelCode;
    private String denied;
    private String dataTax;
}
