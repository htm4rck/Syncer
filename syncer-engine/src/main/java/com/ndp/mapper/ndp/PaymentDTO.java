package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndp.mapper.ndp.others.AuditoryCoreDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDTO extends AuditoryCoreDTO implements Serializable {

    private String payerName;
    private String paymentReceiptId;
    private String nif;
    private String cashRegisterCode;
    private String turnCashRegisterId;
    private String comment;
    private String currency;
    private String payWay;
    private String payWayName;
    private Double amount;
    private Integer migrationStatus;
    private Date migrationDate;
    private String migration;
    private String store;
    private String storeEntry;
    private Integer numberLine;
    private String paymentMethod;
    private String paymentMethodName;
    private String cardType;
    private String voucherNumber;
    private String accountNumber;
    private String bank;
    private String paymentCondition;
    private String accountType;
    private String cardNumber;
    private Double rateValue;
    private Double totalChange;
    private String paymentGroup;
    private Double saleAmount;
    private String saleCurrency;
    private Double remainingAmount;
    private String currencyReceived;
    private Double receivedAmountForeignCurrency;
    private Double receivedAmountNationalCurrency;
    private Double totalReceived;
    private String accountDescription;
    private String creditNote;
    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String codeSeat;

    private String exchangeAccountNumber;
    private Integer installmentId;
    private Double lostAmount;
    private String image;
    private String typeOfQuota;
    private String updateDate;
    private List<PaymentRateDTO> rate;
    private List<PaymentDetailDTO> detail;
    private Integer paymentReceiptCodeERP;
    private String businessPartnerCode;
    private String controlAccount;

    // Qontrols
    private String uniqueIdentifier;
    private String account;
    private String glAccount;
    private String glAccountFrom;
    private String date;
    private String posDate;
    private String paymentType;
    private FundDTO fundDTO;
    private String type;
    private String businessPartner;
    private String businessName;
    private String serialGiftCard;
    private String numberGiftCard;
    private String correlativeGiftCard;
    private String sysNumberGifCard;
    private String giftCardCode;
    private Integer transNum;
    private String codePay;
    private String wareHouse;
    private String docDate;


}
