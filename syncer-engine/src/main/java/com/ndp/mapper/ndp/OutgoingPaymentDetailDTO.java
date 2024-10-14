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
public class OutgoingPaymentDetailDTO implements Serializable {

    private String accountDescription;
    private String accountNumber;
    private Double amount;
    private String bank;
    private String cardNumber;
    private String cardType;
    private Double commission;
    private String currency;
    private String currencyReceived;
    private Integer numberLine;
    private String payWay;
    private String payWayName;
    private String paymentCondition;
    private String paymentMethod;
    private String paymentMethodName;
    private Double rateValue;
    private Double receivedAmountForeignCurrency;
    private Double receivedAmountNationalCurrency;
    private Double remainingAmount;
    private Double saleAmount;
    private String saleCurrency;
    private String status;
    private String store;
    private Double totalChange;
    private Double totalReceived;
    private String voucherNumber;
    private String codePay;

}
