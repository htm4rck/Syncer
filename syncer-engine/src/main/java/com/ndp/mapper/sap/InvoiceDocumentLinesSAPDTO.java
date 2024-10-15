package com.ndp.mapper.sap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.mapper.ndp.PaymentReceiptDetail_POS_DTO;
import com.ndp.mapper.ndp.PaymentReceipt_POS_DTO;
import com.ndp.mapper.sap.constants.Constants_SAP_DTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class InvoiceDocumentLinesSAPDTO implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceDocumentLinesSAPDTO.class);

    private Integer DocEntry;
    private Integer LineNum;
    private String ShipDate;
    private Double Price;
    private Double Rate;
    private Double DiscountPercent;
    private String VendorNum;
    private String SerialNum;
    private Integer SalesPersonCode;
    private Double CommisionPercent;
    private String TreeType;
    private String UseBaseUnits;
    private String SupplierCatNum;
    private String ProjectCode;
    private String BarCode;
    private String VatGroup;
    private Integer BaseType;
    private String BaseEntry;
    private Integer BaseNum;
    private String BaseLine;
    private Double Volume;
    private Integer VolumeUnit;
    private Double Width1;
    private String Width1Unit;
    private String Width2Unit;
    private String Address;
    private String TaxType;
    private String TaxLiable;
    private String PickStatus;
    private Double PickQuantity;
    private String PickListIdNumber;
    private String OriginalItem;
    private Integer ShippingMethod;
    private String CorrectionInvoiceItem;
    private Double CorrInvAmountToStock;
    private Double CorrInvAmountToDiffAcct;
    private Double AppliedTax;
    private Double AppliedTaxFC;
    private Double AppliedTaxSC;
    private String WTLiable;
    private String DeferredTax;
    private Double EqualizationTaxPercent;
    private Double TotalEqualizationTax;
    private Double TotalEqualizationTaxFC;
    private Double TotalEqualizationTaxSC;
    private Double NetTaxAmount;
    private Double NetTaxAmountFC;
    private Double NetTaxAmountSC;
    private Double UnitsOfMeasurment;
    private Double TaxPercentagePerRow;
    private Double TaxTotal;
    private Double TaxPerUnit;
    private Double TotalInclTax;
    private String TransactionType;
    private String PickStatusEx;
    private Double PackageQuantity;
    private String ActualDeliveryDate;
    private String RemainingOpenQuantity;
    private Double OpenAmount;
    private Double OpenAmountFC;
    private Double OpenAmountSC;
    private Double RequiredQuantity;
    private String ReceiptNumber;
    private String FederalTaxID;
    private Integer UoMEntry;
    private String UoMCode;
    private Double InventoryQuantity;
    private String ChangeInventoryQuantityIndependently;
    private String FreeOfChargeBP;
    private Double GrossPrice;
    private Double GrossTotal;
    private Double GrossTotalFC;
    private Double GrossTotalSC;
    private String ShipToCode;
    private String ShipToDescription;
    private String U_VS_DOCSI;
    private Double UnitPrice;
    private String ItemCode;
    private String ItemDescription;
    private Double Quantity;
    private String MeasureUnit;
    private Double PriceAfterVAT;
    private String TaxCode;
    private String AccountCode;
    private Double LineTotal;
    private String Currency;
    private String WarehouseCode;
    private String LocationCode;
    private String U_tipoOpT12;
    private String DocCurrency;
    private String U_BPP_OPER;
    private String U_VS_ONEROSO;
    private String U_VS_TIPAFE;
    private String TaxOnly;
    private String CostingCode;
    private String CostingCode2;
    private String CostingCode3;
    private String CostingCode4;
    private String CostingCode5;
    private List<InvoiceLinesBinAllocation_SAP_DTO> DocumentLinesBinAllocations;
    private List<LineTaxJurisdictionsSAPDTO> LineTaxJurisdictions;

    private String U_NDP_CASO;

    public InvoiceDocumentLinesSAPDTO(PaymentReceiptDetail_POS_DTO paymentReceiptDetail, String currency, PaymentReceipt_POS_DTO paymentReceiptDTO) {
        //logger.info("mapeo de la prd 1.0");

        if (java.util.Objects.equals(paymentReceiptDetail.getIsGrossPrice(), "Y")) {
            //this.PriceAfterVAT = paymentReceiptDetail.getGrossPrice();
            this.UnitPrice = paymentReceiptDetail.getUnitPrice();
        } else {
            /* TODO
             * VALIDAR PARA FUERZA DE VENTAS
             * */
            this.UnitPrice = paymentReceiptDetail.getUnitPrice();
            this.Price = paymentReceiptDetail.getPrice(); // precio unitario con descuento
        }
        //logger.info("mapeo de la prd 1.1");
        /* TODO
         * NO SE PUEDE ENVIAR A SAP 100%, VA EN 0.
         * */
        this.DiscountPercent = paymentReceiptDetail.getDiscountPercentage();
        if (this.DiscountPercent == 100) this.DiscountPercent = 0.0;
        /* TODO
         * Por revisar
         * */
        /*
        if (paymentReceiptDetail.getTaxCode().equals("I18")) {
            this.PriceAfterVAT = paymentReceiptDetail.getGrossPrice();
        } else if (paymentReceiptDetail.getTaxCode().equals("IEX")) {
            this.PriceAfterVAT = paymentReceiptDetail.getUnitPrice();
        }
        */
        //logger.info("mapeo de la prd 1.2");
        this.ItemCode = paymentReceiptDetail.getItemCode();
        this.ItemDescription = paymentReceiptDetail.getItemName();
        this.Quantity = paymentReceiptDetail.getQuantity();
        this.MeasureUnit = paymentReceiptDetail.getUnitMSRCode();
        this.Currency = currency;
        this.WarehouseCode = paymentReceiptDetail.getWarehouseCode();
        this.DocCurrency = paymentReceiptDetail.getCurrency();

        this.U_tipoOpT12 = paymentReceiptDetail.getSunatOperation();
        this.U_BPP_OPER = paymentReceiptDetail.getSunatOperationType();
        this.U_VS_ONEROSO = paymentReceiptDetail.getSunatOneroso();
        this.U_VS_TIPAFE = paymentReceiptDetail.getSunatAffectationType();
        this.TaxOnly = Constants_SAP_DTO.mapNdpFlagToSapFlag(paymentReceiptDetail.getOnlyTax());
        this.UoMEntry = paymentReceiptDetail.getUnitMSREntry() == null ? -1 : paymentReceiptDetail.getUnitMSREntry();
        //logger.info("mapeo de la prd 1.3");

        /* TODO
         * BaseType    - 17 = Orden de Venta
         * BaseRef     - DocNum
         * BaseEntry   - DocEntry
         * BaseLine    - Line
         */

        //logger.info("mapeo de la prd 1.4");


        if (!(paymentReceiptDTO.getSaleOrderCode() == null || paymentReceiptDTO.getSaleOrderCode().isEmpty())) {
            //logger.info("mapeo de la prd 1.4.1; paymentReceiptDTO.getSaleOrderCode(): " + paymentReceiptDTO.getSaleOrderCode() + "; paymentReceiptDTO: " + paymentReceiptDTO.toString() + "; paymentReceiptDetail: " + paymentReceiptDetail.toString());
            this.BaseLine = paymentReceiptDetail.getSaleOrderLine().toString();
            this.BaseEntry = paymentReceiptDetail.getSaleOrderEntry();
            this.BaseNum = paymentReceiptDetail.getSaleOrderDocNum();
            this.BaseType = 17;

            //logger.info("mapeo de la prd 1.4.2");
        }

        //logger.info("mapeo de la prd 1.5");

        this.CostingCode = paymentReceiptDetail.getCostingCode();
        this.CostingCode2 = paymentReceiptDetail.getCostingCode2();
        this.CostingCode3 = paymentReceiptDetail.getCostingCode3();
        this.CostingCode4 = paymentReceiptDetail.getCostingCode4();
        this.CostingCode5 = paymentReceiptDetail.getCostingCode5();
        //this.DiscountPercent = paymentReceiptDetail.getDiscountPercentage();
        //logger.info("mapeo de la prd 1.6");
        this.setU_NDP_CASO(paymentReceiptDetail.getNdpCase());
        if (paymentReceiptDetail.getUbicationEntry() != null && paymentReceiptDetail.getUbicationEntry() != -1) {
            //logger.info("mapeo de la prd 1.6.1");
            InvoiceLinesBinAllocation_SAP_DTO invoiceLinesBinAllocation = new InvoiceLinesBinAllocation_SAP_DTO();
            /* TODO EL LOCATION ENTRY ESTA LLENDO EN DURO EN CASO FALLE??? */
            invoiceLinesBinAllocation.setBinAbsEntry(paymentReceiptDetail.getUbicationEntry());
            invoiceLinesBinAllocation.setQuantity(paymentReceiptDetail.getBaseQuantity());
            this.DocumentLinesBinAllocations = List.of(invoiceLinesBinAllocation);
            //logger.info("mapeo de la prd 1.6.2");
        }
        //logger.info("mapeo de la prd 1.7");
        if (paymentReceiptDetail.getBonuses() == null) paymentReceiptDetail.setBonuses(0.00);
        logger.info("mapeo de la prd 1.8");
        if (paymentReceiptDetail.getDataTax() != null && !paymentReceiptDetail.getDataTax().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                List<PaymentReceiptDetail_POS_DTO> taxDataArray = objectMapper.readValue(paymentReceiptDetail.getDataTax(), new TypeReference<List<PaymentReceiptDetail_POS_DTO>>() {
                });
                this.TaxCode = "";
                this.LineTaxJurisdictions = new ArrayList<>();
                logger.info(" mapeo de detalle de la ov 3.0 ");
                for (PaymentReceiptDetail_POS_DTO taxData : taxDataArray.toArray(PaymentReceiptDetail_POS_DTO[]::new)) {
                    //logger.info("Percepcion {}", new Gson().toJson(taxData));
                    LineTaxJurisdictionsSAPDTO lineTaxJurisdictions = new LineTaxJurisdictionsSAPDTO();
                    lineTaxJurisdictions.setJurisdictionCode(taxData.getTaxCode());
                    lineTaxJurisdictions.setExternalCalcTaxAmount(taxData.getTaxAmount());
                    lineTaxJurisdictions.setExternalCalcTaxRate(taxData.getTax());
                    lineTaxJurisdictions.setJurisdictionType(Integer.parseInt(taxData.getTaxType()));
                    this.TaxCode += taxData.getTaxCode();
                    LineTaxJurisdictions.add(lineTaxJurisdictions);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
