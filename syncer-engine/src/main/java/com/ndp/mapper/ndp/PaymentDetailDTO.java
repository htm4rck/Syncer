package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PaymentDetailDTO implements Serializable {

    private String code;
    private String codeERP;
    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String currency;
    private String payWay;
    private String payWayName;
    private Double amount;
    private Integer status;
    private String store;
    private Integer numberLine;
    private String paymentMethod;
    private String paymentMethodName;
    private String cardType;
    private Double totalChange;
    private Double rateValue;
    private String voucherNumber;
    private String accountNumber;
    private String bank;
    private String paymentCondition;
    private String cardNumber;
    private Double saleAmount;
    private String saleCurrency;
    private Double remainingAmount;
    private String currencyReceived;
    private String accountDescription;
    private Double receivedAmountForeignCurrency;
    private Double receivedAmountNationalCurrency;
    private String paymentGroup;
    private Double totalReceived;
    private String exchangeAccountNumber;
    private Integer installmentId;
    private Double lostAmount;
    private String image;
    private String typeOfQuota;
    private Integer creditCard;
    private String accountType;
}
