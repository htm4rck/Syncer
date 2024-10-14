package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentReceipt_POS_DTO implements Serializable{

    private String businessPartnerCode;
    private String businessPartnerName;
    private String businessPartner;
    private String businessName;
    private Integer docNum;
    private Integer docYear;
    private String serial;
    private String receiptType;
    private Double discount;
    private Integer number;
    private String nif;
    private String storeAddress;
    private String createUser;
    private String createUserName;
    private String createUserEmail;
    private Double discountPercentage;
    private Double total;
    private String currency;
    private Integer documentEntity;
    private String payAddressName;
    private String payAddress;
    private String shipAddressName;
    private String shipAddress;
    private String storeEntry;
    private String transactionalCode;
    private String paymentReceiptNumber;
    private Double subtotal;
    private Double subtotalDiscount;
    private String comment;
    private Double taxAmount;
    private Double tax;
    private String store;
    private String storeName;
    private String process;
    private String receiptTypeName;
    private String cashRegister;
    private String saleType;
    private String paymentCondition;
    private Integer paymentType;
    private Double credit;
    private String returnOrder;
    private Integer paymentMethodType;
    private Integer transNum;
    private String alternativeShipAddress;
    private String tentativeSchedule;
    private Integer detraction;
    private String reservationInvoice;
    private String receiptDate;
    private String receiptDateSync;
    private String docDate;
    private String dueDate;
    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String afeDet;
    private Double porDet;
    private String codSer;
    private Double monDet;
    private String type;
    private Double icbper;
    private Double bonus;
    private String plateNumber;
    private String paymentConditionName;
    private Integer salesPersonCode;
    private String feStat;
    private String feMsg;
    private String hash;
    private String migration;
    private List<SalesOrdersDTO> salesOrders;
    private List<PaymentReceiptDetail_POS_DTO> detail;
    private List<PaymentDTO> payments;
    private List<DocumentInstallmentsDTO> documentInstallments;
    private List<CreditNoteDTO> creditNotesRelations;
    private HashMap<String, Object> additionalData;
    private Integer paymentConditionGroupNumber;
    private Double totalExonerated;
    private String anticipo;
    private String afeRet;
    private Integer numberOfPayments;
    private String documentsubtype;
    private String u_vs_tipo_fact;
    private Integer u_vs_graten;
    private String freeTitleSale;
    private Double rate;
    private Double rateDay;
    private String isExportInvoice;
    private String incoterms;
    private Double totalWeight;
    private String ndpClaDoc;
    private String saleOrderCode;
    private String flReservedCorrelative;
    private String useSerialManual;
    private String isPerception;

    private String consolidatedBusinessPartner;
    private String flConsolidated;
    private String cashRegisterCode;
    private String orderPurchaseBusinessPartner;
    private String deliveryStoreCode;
    private String deliveryStoreName;
    private String deliveryStoreAddress;
    private String deliveryDate;
}
