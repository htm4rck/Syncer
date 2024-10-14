package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndp.mapper.ndp.others.AuditoryCoreDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditNoteDTO extends AuditoryCoreDTO implements Serializable {

    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String paymentReceipt;
    private String returnOrderCode;
    private String reasonSunat;
    private String reasonSunatCode;
    private String createUser;
    private String createUserName;
    private String createUserEmail;
    private Double subtotal;
    private Double tax;
    private Double taxAmount;
    private Double total;
    private String store;
    private String storeName;
    private String cashRegister;
    private String transactionalCode;
    private Integer docNum;
    private Integer docYear;
    private Double earnnigAmount;
    private Double losingAmount;
    private String businessPartnerName;
    private String businessPartnerCode;
    private String businessPartner;
    private String nif;
    private String taxCode;
    //private ReturnOrderDTO returnOrder;
    private String serial;
    private Integer number;
    private String creditNoteNumber;
    private String comment;
    private String payAddressName;
    private String payAddress;
    private String shipAddressName;
    private String shipAddress;
    private String receiptType;
    private Integer transNum;
    private String paymentReceiptNumber;
    private String paymentReceiptType;
    private String paymentReceiptCodeERP;
    private String isReserveInvoice;
    private String controlAccount;
    private String paymentReceiptDate;
    private Double paymentReceiptTotal;
    private Double paymentReceiptBonuses;
    private String docCurrency;
    private String ncNoReturn;	/* para identificar si es => con devolución: 'N' ; sin devolución :'Y'  */
    private List<CreditNoteDetailDTO> detail = new ArrayList<>();
    private String documentDate;
    private String flReservedCorrelative;
    private String freeTitleSale;
    private String orderPurchaseBusinessPartner;
    private String storeEntry;
    private String cashRegisterCode;
    private String isPerception;

}
