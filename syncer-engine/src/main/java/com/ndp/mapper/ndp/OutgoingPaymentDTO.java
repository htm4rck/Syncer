package com.ndp.mapper.ndp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndp.mapper.ndp.others.AuditoryCoreDTO;
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
public class OutgoingPaymentDTO extends AuditoryCoreDTO implements Serializable {

    private Integer erpDocNum;
    private Integer erpSerialNumber;
    private String accountNumber;
    private String businessPartner;
    private String employeeCode;
    private String cashRegister;
    private Double changeRoundingDifference;
    private String comment;
    private String createUser;
    private String createUserEmail;
    private String createUserName;
    private String creditNote;
    private Integer docNum;
    private Integer docYear;
    private String identificationDocument;
    private Double lostAmount;
    private String migration;
    private Integer migrationStatus;
    private String payerName;
    private Double paymentAmount;
    private String process;
    private String caseLoss;
    private Double roundingAmount;
    private String store;
    private String storeEntry;
    private Double totalChange;
    private Double totalReceived;
    private String createUserDocument;
    private String parentDocument;
    private String parentDocumentCode;
    private String parentDocumentCodeERP;
    private String type;
    private String paymentDate; // pattern = "yyyy-MM-dd"
    private String updateDate; // pattern = "yyyy-MM-dd HH:mm:ss"
    private String migrationDate; // pattern = "yyyy-MM-dd HH:mm:ss"
    //private List<OutgoingPaymentRateDTO> rate;
    private List<OutgoingPaymentDetailDTO> detail;

    // Qontrols
    private String uniqueIdentifier;
    private String bank;
    private String account;
    private String glAccount;
    private String glAccountTo;
    private String date;
    private String posDate;
    private String currency;
    private String paymentType;
    private Double amount;
    private FundDTO fundDTO;
    private String businessName;
    private String expenseDocEntry;
    private Integer dailyEntryDocEntry;
    private Integer dailyEntryCountInvoices;

}
