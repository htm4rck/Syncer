package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ndp.mapper.ndp.others.AuditoryCoreDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleOrder_POS_DTO extends AuditoryCoreDTO implements Serializable {

    private String businessPartnerCode;
    private Integer ubicationEntry;
    private String docDate;
    private String deliveryDate;
    private String transactionalCode;
    private String comment;
    private Double subtotal;
    private Double subtotalDiscount;
    private Double discountPercentage;
    private Double discount;
    private Double taxAmount;
    private Double total;
    private String docCurrency;
    private String paymentReceiptType;
    private String ubication;
    private String warehouse;
    private String saleType;
    private String stockMovement;
    private String updateDate;
    private String createUser;
    private String updateUser;
    private String payAddress;
    private String shipAddress;
    private String businessPartnerName;
    private String emailSalePerson;
    private String nif;
    private String payAddressName;
    private String shipAddressName;
    private String migration;
    private Integer migrationStatus;
    private String migrationDate;
    private String sampleCode;
    private String pickingStatus;
    private Integer isDispatch;
    private String nameSalePerson;
    private Integer docYear;
    private Integer docNum;
    private String quotationCode;
    private String store;
    private String storeAddress;
    private String storeEntry;
    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String U_VS_AFEDET;
    private Double U_VS_PORDET;
    private String U_VS_CODSER;
    private Double U_VS_MONDET;
    private Double ICBPER;
    private Double BONUS;
    private String plateNumber;
    private String paymentConditionName;
    private Integer salesPersonCode;
    private Integer paymentConditionGroupNumber;
    private String orderPurchaseBusinessPartner;
    private String businessPartner;
    private String businessName;
    private String channelBusiness;
    private String projectCode;
    private String channelCode;
    private String channelApp;
    private String channelAppName;
    private String saleClassification;
    private String statusSAP;
    private String statusLogistic;
    private String shippingDate;
    private String uploadDate;
    private String purchaseTerm;
    private String containerCode;
    private String dutyFreeZone;
    private String exportDestination;
    private String deliveryThirdParty;
    private String deliveryDateTo;
    private String flAffectionDetraction;
    private Double percentageDetraction;
    private String isExportInvoice;
    private String freeTitleSale;
    private String flIsTransportAgency;
    private String transportAgencyCode;
    private String transportAgencyName;
    private String transportAgencyAddress;
    private String transportAgencyAddressName;
    private String customsWarehouseCode;
    private String customsWarehouseName;
    private String customsWarehouseAddress;
    private List<SaleOrderDetail_POS_DTO> detail;
    private String statusOperation;
    private String idVtex;
    private String isPerception;

}
