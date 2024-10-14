package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundDTO implements Serializable {
    private String codeERP;
    private String code;
    private String name;
    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String userUniqueId;
    private String uniqueIdentifier;
    private Long id;
    private String financeUniqueId; // Financiero
    private String rol;
    private String businesspartner;
    private String description;
    private String currency;
    private Double amount;
    private Double amountCharged;
    private Double amountAvailable;
    private Double amountChargedForecast;
    private Double amountAvailableForecast;
    private String fundType;
    private String account;
    private String fundAccount;
    private String companyCodeFund;
    private String statusOperation;
    private String commentRej;
    private String close;
    private String settlement;
    private String fundAccountTo;
    private List<OutgoingPaymentDTO> outgoingPaymentDTOlist;
    private List<PaymentDTO> paymentDTOS;
    private String docEntryReturn;

}
