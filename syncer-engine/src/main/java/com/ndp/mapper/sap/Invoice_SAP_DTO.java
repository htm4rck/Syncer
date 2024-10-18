package com.ndp.mapper.sap;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ndp.mapper.ndp.DocumentInstallmentsDTO;
import com.ndp.mapper.ndp.PaymentReceipt_POS_DTO;
import com.ndp.mapper.sap.constants.Constants_SAP_DTO;
import com.ndp.mapper.sap.util.Formatters;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class Invoice_SAP_DTO  implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Invoice_SAP_DTO.class);

    private String DocumentSubType;
    private String ReserveInvoice;
    private Integer DocEntry;
    private Integer DocNum;
    private Integer Series;
    private String DocType;
    private String HandWritten;
    private String Printed;
    private Double DocTotal;
    private Double PaidToDate;
    private Double DocTotalFC;
    private Double PaidFC;
    private String DocCurrency;
    private Double DocRate;
    private String Reference1;
    private String Reference2;
    private String Comments;
    private Integer SalesPersonCode;
    private Integer TransportationCode;
    private String Confirmed;
    private Double ImportFileNum;
    private String SummeryType;
    private Integer ContactPersonCode;
    private String ShowSCN;
    private String PartialSupply;
    private String DocObjectCode;
    private Double TotalDiscount;
    private Double DiscountPercent;
    private String CreationDate;
    private String UpdateDate;
    private Integer TransNum;
    private Double VatSum;
    private Double VatSumSys;
    private Double VatSumFc;
    private String NetProcedure;
    private Double DocTotalFc;
    private Double DocTotalSys;
    private String RevisionPo;
    private String BlockDunning;
    private String Submitted;
    private Integer Segment;
    private Integer NumberOfInstallments;

    private String CardCode;
    private String CardName;
    private String FederalTaxID;
    private String DocDate;
    private String DocTime;
    private String DocDueDate;
    private String TaxDate;
    private String JournalMemo;
    private Integer PaymentGroupCode;
    private String PayToCode;
    private String Address;
    private String Address2;
    private String ShipToCode;
    private String NumAtCard;
    private String U_BPV_SERI;
    private String U_BPV_NCON2;
    private String U_BPP_MDTD;
    private String U_VS_USRSV;
    private String U_BPP_MDSD;
    private String U_VS_NRO_FT;
    private String U_BPP_MDCD;
    private String U_VS_AFEDET;
    private Double U_VS_PORDET;
    private String U_VS_CODSER;
    private Double U_VS_MONDET;
    private String U_NDP_CODE;
    private String U_VS_TIPO_FACT;
    private Integer U_CL_TIPOVEN;
    private String U_CL_TIPFAC; //Tipo de facturación NOR: Normal CON: Consignacion RES : Reserva
    private String U_CL_TSCHEDULE;
    private String U_VS_FESTAT;
    /**
     * este campo corresponde al estado de SUNAT
     */
    private String U_VS_DIGESTVALUE;
    /**
     * este campo corresponde al hash
     */
    private String U_NDP_MIG_EST;
    private String U_NDP_MIG_FEC;
    private String U_NDP_MIG_MSJ;

    private Integer U_VS_GRATEN;
    private String U_CL_NOMALT;
    //tipo de cambio informativo
    private Double U_NDP_TC;
    private String U_CL_ICTRM;
    private String U_CL_PNETO;
    private String U_VS_TIPOPER;
    private String U_NDP_CLADOC;
    private String U_VS_CDRRSM;
    private String U_CL_CONTIN;
    private String U_VS_INCPRCP;// PERCEPCION
    private String U_VS_METVAL; //Metodo Validación, B- Boleta, F-Factura , N- Ninguno (PERCEPCION)
    private String FatherCard;
    private String FatherType;
    private String U_CL_CODEMP;
    private String U_NDP_CODCAJ;
    private String U_NDP_CASO;

    /**
     * este campo corresponde al mensaje de respuesta SUNAT
     */
    private List<InvoiceDocumentLinesSAPDTO> DocumentLines;
    private List<DocumentInstallments_SAP_NDP_DTO> DocumentInstallments;


    public Invoice_SAP_DTO(PaymentReceipt_POS_DTO paymentReceipt) {
        this.U_NDP_CODCAJ = paymentReceipt.getCashRegisterCode()==null?"":paymentReceipt.getCashRegisterCode();
        PaymentReceipt_POS_DTO paymentReceiptDTO = (PaymentReceipt_POS_DTO) paymentReceipt;
        this.U_CL_CODEMP = paymentReceiptDTO.getStoreEntry() == null ? "" : paymentReceiptDTO.getStoreEntry();
        paymentReceiptDTO.setDiscountPercentage(paymentReceiptDTO.getDiscountPercentage() != null ? paymentReceiptDTO.getDiscountPercentage() : 0);
        this.CardName = paymentReceiptDTO.getBusinessPartnerName().length() > 100 ? // 100 chars
                paymentReceiptDTO.getBusinessPartnerName().substring(0, 100) :
                paymentReceiptDTO.getBusinessPartnerName();
        this.JournalMemo = paymentReceiptDTO.getBusinessPartnerName().length() > 50 ? // 50 chars
                paymentReceiptDTO.getBusinessPartnerName().substring(0, 50) :
                paymentReceiptDTO.getBusinessPartnerName();

        if (paymentReceiptDTO.getFlConsolidated() != null && paymentReceiptDTO.getFlConsolidated().equals("Y")) {
            this.FatherCard = paymentReceiptDTO.getConsolidatedBusinessPartner();
            this.FatherType = "cPayments_sum";
        }
        this.U_VS_INCPRCP = paymentReceiptDTO.getIsPerception() == null ? "N" : paymentReceiptDTO.getIsPerception();
        this.CardCode = paymentReceiptDTO.getBusinessPartnerCode();
        this.FederalTaxID = paymentReceiptDTO.getNif();
        this.DocCurrency = paymentReceiptDTO.getCurrency();

        this.U_NDP_TC = paymentReceiptDTO.getRateDay();
        this.DocDate = paymentReceiptDTO.getDocDate() != null ? paymentReceiptDTO.getDocDate() : paymentReceiptDTO.getCreateDate();
        this.DocDueDate = paymentReceiptDTO.getDueDate() != null ? paymentReceiptDTO.getDueDate() : paymentReceiptDTO.getCreateDate();
        this.TaxDate = paymentReceiptDTO.getDocDate() != null ? paymentReceiptDTO.getDocDate() : paymentReceiptDTO.getCreateDate();
        this.PaymentGroupCode = paymentReceiptDTO.getPaymentConditionGroupNumber() == null ? -1 : paymentReceiptDTO.getPaymentConditionGroupNumber();
        this.PayToCode = paymentReceiptDTO.getPayAddressName();
        this.ShipToCode = paymentReceiptDTO.getShipAddressName();
        String documentTypeCode = paymentReceiptDTO.getReceiptType();
        String serialNumber = paymentReceiptDTO.getSerial();
        String correlativeNumber = Formatters.formatNumberAddLeadingZeros(paymentReceiptDTO.getNumber(), 8);
        this.NumAtCard = String.format("%s%s-%s", documentTypeCode, serialNumber, correlativeNumber);
        this.SalesPersonCode = paymentReceiptDTO.getSalesPersonCode() == null ? -1 : paymentReceiptDTO.getSalesPersonCode();
        this.U_BPV_SERI = String.format("%s-%s", serialNumber, documentTypeCode);
        this.U_BPV_NCON2 = correlativeNumber;
        this.U_BPP_MDTD = documentTypeCode;

        if (paymentReceiptDTO.getFlReservedCorrelative() == null) paymentReceiptDTO.setFlReservedCorrelative("N");
        this.U_VS_USRSV = "Y";
        this.U_BPP_MDSD = serialNumber;
        this.U_BPP_MDCD = correlativeNumber;
        this.U_NDP_CODE = paymentReceiptDTO.getId();
        this.U_NDP_CLADOC = paymentReceiptDTO.getNdpClaDoc();
        this.U_CL_NOMALT = paymentReceiptDTO.getBusinessName() == null ? "" : paymentReceiptDTO.getBusinessName();

        if (paymentReceiptDTO.getAfeRet() == null) {
            paymentReceiptDTO.setAfeRet("N");
        }
        if (paymentReceiptDTO.getAfeDet() == null) {
            paymentReceiptDTO.setAfeDet("N");
        }
        if (paymentReceiptDTO.getPorDet() == null) {
            paymentReceiptDTO.setPorDet(0.0);
        }
        if (paymentReceiptDTO.getCodSer() == null) {
            paymentReceiptDTO.setCodSer("");
        }
        if (paymentReceiptDTO.getMonDet() == null) {
            paymentReceiptDTO.setMonDet(0.0);
        }

        this.Comments = paymentReceiptDTO.getComment();
        this.U_VS_AFEDET = paymentReceiptDTO.getAfeDet();
        this.U_VS_PORDET = paymentReceiptDTO.getPorDet();
        this.U_VS_CODSER = paymentReceiptDTO.getCodSer();
        this.U_VS_MONDET = paymentReceiptDTO.getMonDet();
        this.U_CL_TSCHEDULE = paymentReceiptDTO.getTentativeSchedule();
        paymentReceiptDTO.setUseSerialManual(paymentReceiptDTO.getUseSerialManual() == null ? "N" : paymentReceiptDTO.getUseSerialManual());
        if (paymentReceiptDTO.getUseSerialManual().equals("Y")) {
            this.U_VS_FESTAT = "K";
        }else{
            this.U_VS_FESTAT = paymentReceiptDTO.getFeStat();
        }
        this.U_VS_DIGESTVALUE = paymentReceiptDTO.getHash();
        this.U_NDP_MIG_EST = Constants_SAP_DTO.MIGRATION_STATUS_FINALIZED;
        this.U_NDP_MIG_FEC = Formatters.formatDateToSAPString(new Date());
        this.U_NDP_MIG_MSJ = Constants_SAP_DTO.MIGRATION_MESSAGE_COMPLETED;

        if (paymentReceiptDTO.getReservationInvoice() != null && (paymentReceiptDTO.getReservationInvoice().equals("Y"))) {
            this.ReserveInvoice = "tYES";
        }
        /* TODO
         * 0101 - Venta Interna (Deberia ser por defecto);
         * 1001 - Sujeta a Detracion;
         * 1002 - Sujeta a Detracion - Recursos Hidrobiologicos;
         * 1003 - Sujeta a Detracion - Recursos Transporte de Pasajeros;
         * 1004 - Sujeta a Detracion - Recursos Transporte de Carga (confirmar si se tiene que especificar);
         */
        if (paymentReceiptDTO.getAfeDet() != null && (paymentReceiptDTO.getAfeDet().equals("Y"))) {
            this.U_VS_TIPO_FACT = "1001";
        }

        if (paymentReceiptDTO.getIsExportInvoice() != null && (paymentReceiptDTO.getIsExportInvoice().equals("Y"))) {
            this.U_VS_TIPO_FACT = "0200";
            this.U_VS_TIPOPER = "01";
            this.U_CL_PNETO = String.valueOf(paymentReceiptDTO.getTotalWeight());
            if (paymentReceiptDTO.getIncoterms() != null) this.U_CL_ICTRM = paymentReceiptDTO.getIncoterms();

        }

        if (paymentReceiptDTO.getCurrency() != null && (!paymentReceiptDTO.getCurrency().equals("SOL"))) {
            this.DocRate = paymentReceiptDTO.getRate();
        }

        this.U_VS_INCPRCP = paymentReceiptDTO.getIsPerception() == null ? "N" : paymentReceiptDTO.getIsPerception();
        if (this.U_VS_INCPRCP.equals("Y")) {
            this.U_VS_METVAL = documentTypeCode.equals("01") ? "F" : "B";
        }

        this.DocumentLines = new ArrayList<>();
        paymentReceiptDTO.getDetail().forEach(detail -> this.DocumentLines.add(new InvoiceDocumentLinesSAPDTO(detail, this.DocCurrency, paymentReceiptDTO)));
        if (paymentReceiptDTO.getAfeDet().equals("Y") || paymentReceiptDTO.getAfeRet().equals("Y")) {
            this.DocumentSubType = "dDocument_Items";
            this.DocumentInstallments = new ArrayList<>();
            /* TODO
             * Cuotas en caso de Detraacion, Retencion.
             */
            for (DocumentInstallmentsDTO documentInstallmentsDTO : paymentReceiptDTO.getDocumentInstallments()) {
                DocumentInstallments_SAP_NDP_DTO t = new DocumentInstallments_SAP_NDP_DTO();
                t.setDueDate(Formatters.formatDateAsStringByPattern(documentInstallmentsDTO.getDueDate(), "yyyy-MM-dd"));
                t.setPercentage(documentInstallmentsDTO.getPercentage());
                t.setU_VS_TIPOCUOTA(documentInstallmentsDTO.getType());
                t.setU_VS_TIPOSERVICIO(documentInstallmentsDTO.getTypeService());
                this.DocumentInstallments.add(t);
            }

        }

        if (System.getenv().get("company") != null && System.getenv().get("company").equals("BASA")) {
            this.U_CL_TIPFAC = paymentReceiptDTO.getSaleType();
        } else if (paymentReceiptDTO.getCompanyCode() != null && paymentReceiptDTO.getCompanyCode().equals("AZALEIA")) {
            this.JournalMemo = "Ventas tienda-"+paymentReceiptDTO.getStoreName();
        }

        if (paymentReceiptDTO.getFreeTitleSale().equals("Y")) {
            this.DocumentSubType = paymentReceiptDTO.getDocumentsubtype();
            this.U_VS_GRATEN = 2;
            this.U_VS_TIPO_FACT = "0101";
        } else {
            this.U_VS_GRATEN = 1;
        }

        U_CL_CONTIN = (paymentReceiptDTO.getUseSerialManual().equals("Y")) ? "Y" : "N";
        this.setU_NDP_CASO(paymentReceiptDTO.getNdpCase());


    }
    /*
    @Override
    public String getAbsoluteURI(String objectPath) {
        return null;
    }

    @Override
    public NdpObject toNdpObject() {
        return null;
    }

    @Override
    public ObjectIdentifierValuePair getIdentifierValuePair() {
        return null;
    }

    @Override
    public NdpObject mapProviderPostResponseToNdpObject() {
        PaymentReceipt_POS_DTO paymentReceipt = new PaymentReceipt_POS_DTO();
        paymentReceipt.setId(this.U_NDP_CODE);
        paymentReceipt.setCodeERP(this.DocEntry.toString());
        paymentReceipt.setErpDocNum(this.DocNum);
        paymentReceipt.setErpSerialNumber(this.Series);
        paymentReceipt.setTransNum(this.TransNum);
        paymentReceipt.setFeStat(this.U_VS_FESTAT);
        return paymentReceipt;
    }

    @Override
    public Object getCompletedMigrationObject(boolean successful, Object ndpObject) {
        Invoice_SAP_GPC_DTO invoice_sap_gpc_dto = new Invoice_SAP_GPC_DTO();
        BeanUtils.copyProperties(
                successful ? Migration_SAP_DTO.buildSucceededMigration() : Migration_SAP_DTO.buildFailedMigration(),
                invoice_sap_gpc_dto
        );
        if (successful) {
            invoice_sap_gpc_dto.setDocEntry(Integer.parseInt(MappersGatewayHttps.mapObjectToCastedByClassname(ndpObject, PaymentReceipt_POS_DTO.class).getCodeERP()));
            invoice_sap_gpc_dto.setU_NDP_CODE(MappersGatewayHttps.mapObjectToCastedByClassname(ndpObject, PaymentReceipt_POS_DTO.class).getId());
        }
        return invoice_sap_gpc_dto;
    }


    @Override
    public PaymentReceipt_POS_DTO toPaymentReceiptDTO() {
        try {
        PaymentReceipt_POS_DTO paymentReceipt = new PaymentReceipt_POS_DTO();
        paymentReceipt.setId(this.U_NDP_CODE);
        paymentReceipt.setDiscountPercentage(this.DiscountPercent);
        paymentReceipt.setBusinessPartnerName(this.CardName);
        paymentReceipt.setBusinessPartnerName(this.CardName);
        paymentReceipt.setBusinessPartner(this.CardCode);
        paymentReceipt.setNif(this.FederalTaxID);
        paymentReceipt.setCurrency(this.DocCurrency);
        paymentReceipt.setTransactionalCode(FormattersGatewayHttps.getTransactionalCode());
        paymentReceipt.setPayAddressName(this.PayToCode);
        paymentReceipt.setPayAddress(this.Address2);
        paymentReceipt.setShipAddressName(this.ShipToCode);
        paymentReceipt.setShipAddress(this.Address);
        paymentReceipt.setReceiptType(this.U_BPP_MDTD);
        paymentReceipt.setSerial(this.U_BPP_MDSD);
        paymentReceipt.setNumber(Integer.parseInt(this.U_BPP_MDCD));
        paymentReceipt.setPaymentReceiptNumber(String.format("%s-%s", this.U_BPP_MDSD, this.U_BPP_MDCD));
        paymentReceipt.setAfeDet(this.U_VS_AFEDET);
        paymentReceipt.setPorDet(this.U_VS_PORDET);
        paymentReceipt.setCodSer(this.U_VS_CODSER);
        paymentReceipt.setMonDet(this.U_VS_MONDET);
        paymentReceipt.setSaleType(this.U_CL_TIPFAC);
        paymentReceipt.setTentativeSchedule(this.U_CL_TSCHEDULE);
        paymentReceipt.setFeStat(this.U_VS_FESTAT);
        paymentReceipt.setDiscount(this.TotalDiscount);
        paymentReceipt.setDiscountPercentage(this.DiscountPercent);
        paymentReceipt.setCreateUser(GatewayHttpsConstants.SYNCHRONIZER_MEMBER_ID);
        paymentReceipt.setCreateUserEmail(GatewayHttpsConstants.SYNCHRONIZER_USERNAME);
        paymentReceipt.setCreateUserName(GatewayHttpsConstants.SYNCHRONIZER_NAME);
        paymentReceipt.setSubtotalDiscount(0.0);
        paymentReceipt.setSubtotal(0.0);
        paymentReceipt.setTaxAmount(0.0);
        paymentReceipt.setTotal(this.DocTotal);
        paymentReceipt.setComment(this.Comments);
        paymentReceipt.setStatus("CREATED");
        paymentReceipt.setTax(0.0);
        paymentReceipt.setStore("STORE-SAP");
        paymentReceipt.setReceiptDateSync(this.DocDate + " " + this.DocTime);
        paymentReceipt.setMigration(Constants_SAP_DTO.MIGRATION_NO);
        paymentReceipt.setProcess("COMPROBANTE_DE_PAGO");
        paymentReceipt.setSaleType(this.U_CL_TIPFAC);
        paymentReceipt.setStoreAddress("-");
        paymentReceipt.setPaymentCondition("");
        paymentReceipt.setPaymentType(1);
        paymentReceipt.setCredit(0.0);
        paymentReceipt.setCodeERP(this.DocEntry.toString());
        paymentReceipt.setErpDocNum(this.DocNum);
        paymentReceipt.setErpSerialNumber(this.Series);
        paymentReceipt.setTransNum(this.TransNum);
        paymentReceipt.setFeStat(this.U_VS_FESTAT);
        paymentReceipt.setHash(this.U_VS_DIGESTVALUE);
        paymentReceipt.setCodSer(this.U_VS_CODSER);
        paymentReceipt.setMonDet(this.U_VS_MONDET);
        paymentReceipt.setAfeDet(this.U_VS_AFEDET);
        paymentReceipt.setBonus(0.0);
        paymentReceipt.setIcbper(0.0);
        paymentReceipt.setPaymentCondition("");
        paymentReceipt.setPaymentConditionName("");
        paymentReceipt.setTransNum(null);
        paymentReceipt.setAlternativeShipAddress("");
        paymentReceipt.setTentativeSchedule(this.U_CL_TSCHEDULE);
        if (this.DocRate != null) {
            paymentReceipt.setRate(this.DocRate);
        }
        paymentReceipt.setRateDay(this.U_NDP_TC);
        paymentReceipt.setErpDocNum(this.DocNum);
        if (this.U_VS_TIPO_FACT != null) {
            paymentReceipt.setAfeDet(this.U_VS_TIPO_FACT.equals("1001") ? "Y" : "N");
            paymentReceipt.setDetraction(paymentReceipt.getAfeDet().equals("Y") ? 1 : 0);
        }
        if (this.ReserveInvoice != null) {
            paymentReceipt.setReservationInvoice(this.ReserveInvoice.equals("tYES") ? "1" : "0");
        }
        //not installments documents (upgrade in next version)
        List<PaymentReceiptDetail_POS_DTO> detail = new ArrayList<>();
        for (int i = 0; i < this.DocumentLines.size(); i++) {
            InvoiceDocumentLinesSAPDTO invoiceDocumentLine = this.DocumentLines.get(i);
            PaymentReceiptDetail_POS_DTO paymentReceiptDetailDTO = new PaymentReceiptDetail_POS_DTO();
            paymentReceiptDetailDTO.setNumberLine(i);
            paymentReceiptDetailDTO.setGrossPrice(invoiceDocumentLine.getPriceAfterVAT()); // precio con impuesto sin descuento (validar que campo es en sap)
            paymentReceiptDetailDTO.setGrossPriceDiscount(invoiceDocumentLine.getPriceAfterVAT());
            paymentReceiptDetailDTO.setItemCode(invoiceDocumentLine.getItemCode());
            paymentReceiptDetailDTO.setItemName(invoiceDocumentLine.getItemDescription());
            paymentReceiptDetailDTO.setQuantity(invoiceDocumentLine.getQuantity());
            paymentReceiptDetailDTO.setUnitMSRCode(invoiceDocumentLine.getMeasureUnit());
            paymentReceiptDetailDTO.setTaxCode(invoiceDocumentLine.getTaxCode());
            paymentReceiptDetailDTO.setCurrency(this.DocCurrency);
            paymentReceiptDetailDTO.setDiscountPercentage(invoiceDocumentLine.getDiscountPercent());
            paymentReceiptDetailDTO.setSunatOperation(invoiceDocumentLine.getU_tipoOpT12());
            paymentReceiptDetailDTO.setSunatOperationType(invoiceDocumentLine.getU_BPP_OPER());
            paymentReceiptDetailDTO.setSunatOneroso(invoiceDocumentLine.getU_VS_ONEROSO());
            paymentReceiptDetailDTO.setSunatAffectationType(invoiceDocumentLine.getU_VS_TIPAFE());
            paymentReceiptDetailDTO.setOnlyTax(Constants_SAP_DTO.mapSapFlagToNdp(invoiceDocumentLine.getTaxOnly()));
            paymentReceiptDetailDTO.setUnitMSREntry(invoiceDocumentLine.getUoMEntry());
            paymentReceiptDetailDTO.setSaleOrderCode(String.valueOf(invoiceDocumentLine.getBaseLine()));
            paymentReceiptDetailDTO.setSaleOrderEntry(invoiceDocumentLine.getBaseEntry());
            paymentReceiptDetailDTO.setUnitGroupEntry(-1);
            paymentReceiptDetailDTO.setTax(invoiceDocumentLine.getTaxTotal());
            paymentReceiptDetailDTO.setTotal(invoiceDocumentLine.getLineTotal());
            if (invoiceDocumentLine.getDocumentLinesBinAllocations() != null && !invoiceDocumentLine.getDocumentLinesBinAllocations().isEmpty()) {
                InvoiceLinesBinAllocation_SAP_GPC_DTO ubicationTo = invoiceDocumentLine.getDocumentLinesBinAllocations().get(0);
                paymentReceiptDetailDTO.setUbicationEntry(ubicationTo.getBinAbsEntry());
                paymentReceiptDetailDTO.setQuantity(ubicationTo.getQuantity());
            }
            detail.add(paymentReceiptDetailDTO);
        }
        System.out.println("Paso 18");
        paymentReceipt.setSalesPersonCode(this.SalesPersonCode);
        paymentReceipt.setDetail(detail);
        paymentReceipt.setNdpCase(this.getU_NDP_CASO());
        return paymentReceipt;
        } catch (NullPointerException e) {
            this.logErrorDetails(e);
            return null;
        }
    }

    @Override
    public String assembleAbsoluteURI(String path) {
        return String.format(
                "%s(%s)",
                path,
                this.DocEntry
        );
    }*/

    private void logErrorDetails(NullPointerException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement cause = stackTrace[0];

        String lineOfCode = "";
        try {
            String filePath = "src/main/java/com/ndp/mapper/models/standart/b1s_v1." + cause.getClassName().replace('.', '/') + ".java";
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            lineOfCode = lines.get(cause.getLineNumber() - 1).trim(); // Obteniendo la línea del archivo
        } catch (Exception ex) {
            lineOfCode = "No se pudo obtener la línea de código.";
        }

        // Logueando la información detallada junto con la línea de código
        logger.error("NullPointerException in method: {} at line: {} in field: {} \nCode: '{}'",
                cause.getMethodName(), cause.getLineNumber(), cause.getFileName(), lineOfCode, e);
    }

}
