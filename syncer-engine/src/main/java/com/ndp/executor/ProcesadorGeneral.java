package com.ndp.executor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ndp.entity.syncer.Business;
import com.ndp.service.rest.NDPServices;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

interface Procesable {
    String getId(); // Obtener el ID del objeto

    void procesar(SAPService sapService); // Método para procesar el objeto

    void procesar(NDPServices ndpServices);
}

public class ProcesadorGeneral {
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(ProcesadorGeneral.class);
    private static final int NUM_THREADS = 4;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static <T extends Procesable> void procesarObjetosEnParalelo(List<T> objetos, String nombreObjeto) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
        CompletionService<Void> completionService = new ExecutorCompletionService<>(threadPool);
        SAPService sapService = new SAPService();
        Business business = new Business();
        business.setPath("https://lazzos.services.360salesolutions.com/");
        business.setPass("Admin@9876");
        business.setUser("mpereyra@lazzos.com.pe");
        business.setCompany("LAZZOS");
        NDPServices ndpServices = new NDPServices(business);
        for (T objeto : objetos) {
            completionService.submit(() -> {
                procesarObjeto(objeto, nombreObjeto, sapService, ndpServices);
                return null;
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Thread was interrupted", e);
            }
        }

        threadPool.shutdown();

        try {
            for (int i = 0; i < objetos.size(); i++) {
                Future<Void> future = completionService.take(); // Esperar la siguiente tarea en completarse
                try {
                    future.get(); // Obtener el resultado para asegurarse de que no hubo excepciones
                } catch (ExecutionException e) {
                    logger.warn("Error en el procesamiento de una operación", e.getCause());
                }
            }
        } catch (InterruptedException e) {
            logger.warn("Interrupción durante la espera de tareas", e);
            threadPool.shutdownNow();
        }
    }

    public static <T extends Procesable> void procesarObjeto(T objeto, String nombreObjeto, SAPService sapService, NDPServices ndpServices) {
        try {
            String threadName = Thread.currentThread().getName();
            String currentTime = LocalDateTime.now().format(formatter);
            if (objeto instanceof StockTransfer) {
                objeto.procesar(ndpServices);
            } else {
                objeto.procesar(sapService);
            }
            objeto.procesar(sapService);
            currentTime = LocalDateTime.now().format(formatter);
            logger.warn("[" + currentTime + "], Hilo: " + threadName + ", Objeto: " + nombreObjeto + ", terminó de procesar el objeto, ID " + objeto.getId());
        } catch (Exception e) {
            logger.warn("Error procesando objeto ID: " + objeto.getId() + " - " + e.getMessage(), e);
        }
    }


    public static List<Factura> obtenerFacturas() {
        String archivoCSV = "facturas.csv";
        String linea = "";
        String separadorCSV = ";";

        List<Factura> listaRegistros = new ArrayList<>();

        try (InputStream inputStream = ProcesadorGeneral.class.getClassLoader().getResourceAsStream(archivoCSV); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String encabezado = br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(separadorCSV);
                String correlativo = columnas[0];
                Factura registro = new Factura(correlativo);
                listaRegistros.add(registro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaRegistros;
    }

    public static List<StockTransfer> obtenerTransferencias() throws IOException {
        List<StockTransfer> listaRegistros = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream inputStream = ProcesadorGeneral.class.getClassLoader().getResourceAsStream("stockTransfer360.json")) {
            if (inputStream == null) {
                throw new IOException("Archivo no encontrado: stockTransfer360.json");
            }

            // Convert InputStream to String
            String jsonString;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                jsonString = reader.lines().collect(Collectors.joining("\n"));
            }
            if (jsonString.isEmpty()) {
                throw new IOException("El archivo stockTransfer360.json está vacío");
            }

            StockTransfer stockTransfer = mapper.readValue(jsonString, StockTransfer.class);
            stockTransfer.setCadena(mapper.readValue(jsonString, Object.class)); // Set the JSON string

            // Create multiple copies of the StockTransfer object
            for (int i = 0; i < 2024; i++) { // Change 2 to the desired number of copies
                listaRegistros.add(stockTransfer);
            }
        }

        return listaRegistros;
    }

    // Todo: Descomentar para ejecutar la aplicación
    /*public void onStart(@Observes StartupEvent ev) throws InterruptedException, IOException {

        try {
            FileHandler fileHandler = new FileHandler("mi_log.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            //logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String currentTime = LocalDateTime.now().format(formatter);  // Obtener la fecha y hora actuales
        // 1. Obtener listas de objetos a procesar
        //List<Factura> facturas = obtenerFacturas();
        //List<Pago> pagos = obtenerPagos();
        //List<NotaDeCredito> notasDeCredito = obtenerNotasDeCredito();
        List<StockTransfer> transferencias = obtenerTransferencias();
        // 2. Procesar cada tipo de objeto en secuencia pero en paralelo para cada tipo
        logger.warn("[" + currentTime + "], Inicio Sincronización Objetos,,,");
        logger.warn("[" + currentTime + "], Sincronizando Facturas,,,");
        //procesarObjetosEnParalelo(facturas, "facturas");
        logger.warn("[" + currentTime + "], Sincronizando Pagos,,,");
        //procesarObjetosEnParalelo(pagos,"pagos");
        logger.warn("[" + currentTime + "], Sincronizando Notas de crédito,,,");
        //procesarObjetosEnParalelo(notasDeCredito,"nc");
        procesarObjetosEnParalelo(transferencias, "transferencias");
    }*/

    public static List<Pago> obtenerPagos() {
        String archivoCSV = "pagos.csv";
        String linea = "";
        String separadorCSV = ";";

        List<Pago> listaRegistros = new ArrayList<>();

        try (InputStream inputStream = ProcesadorGeneral.class.getClassLoader().getResourceAsStream(archivoCSV); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String encabezado = br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(separadorCSV);
                String correlativo = columnas[0];
                Pago registro = new Pago(correlativo, "", 10);
                listaRegistros.add(registro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaRegistros;
    }

    public static List<NotaDeCredito> obtenerNotasDeCredito() {
        String archivoCSV = "nc.csv";
        String linea = "";
        String separadorCSV = ";";

        List<NotaDeCredito> listaRegistros = new ArrayList<>();

        try (InputStream inputStream = ProcesadorGeneral.class.getClassLoader().getResourceAsStream(archivoCSV); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String encabezado = br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split(separadorCSV);
                String correlativo = columnas[0];
                NotaDeCredito registro = new NotaDeCredito(correlativo, "", 10);
                listaRegistros.add(registro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaRegistros;
    }
}

// Clase Factura que implementa Procesable
@Getter
@Setter
class Factura implements Procesable {
    private String id;
    @JsonProperty("cliente")
    private String cliente;
    @JsonProperty("monto")
    private double monto;
    @JsonProperty("ReserveInvoice")
    private String ReserveInvoice = "tYES";
    @JsonProperty("Comments")
    private String Comments = "";
    @JsonProperty("SalesPersonCode")
    private int SalesPersonCode = -1;
    @JsonProperty("CardCode")
    private String CardCode = "CL20601764173";
    @JsonProperty("CardName")
    private String CardName = "ABY BOUTIQUE S.R.L.";
    @JsonProperty("FederalTaxID")
    private String FederalTaxID = "20601764173";
    @JsonProperty("DocDate")
    private String DocDate = "2024-09-30";
    @JsonProperty("DocDueDate")
    private String DocDueDate = "2024-09-30";
    @JsonProperty("TaxDate")
    private String TaxDate = "2024-09-30";
    @JsonProperty("JournalMemo")
    private String JournalMemo = "ABY BOUTIQUE S.R.L.";
    @JsonProperty("PaymentGroupCode")
    private int PaymentGroupCode = -1;
    @JsonProperty("PayToCode")
    private String PayToCode = "FISCAL";
    @JsonProperty("ShipToCode")
    private String ShipToCode = "ALMACEN Anexo 0";
    @JsonProperty("U_VS_INCPRCP")
    private String U_VS_INCPRCP = "N";
    @JsonProperty("NumAtCard")
    private String NumAtCard;
    @JsonProperty("U_BPV_SERI")
    private String U_BPV_SERI;
    @JsonProperty("U_BPV_NCON2")
    private String U_BPV_NCON2;
    @JsonProperty("U_VS_USRSV")
    private String U_VS_USRSV = "N";
    @JsonProperty("U_VS_AFEDET")
    private String U_VS_AFEDET = "N";
    @JsonProperty("U_VS_PORDET")
    private double U_VS_PORDET = 0.0;
    @JsonProperty("U_VS_CODSER")
    private String U_VS_CODSER = "";
    @JsonProperty("U_VS_MONDET")
    private double U_VS_MONDET = 0.0;
    @JsonProperty("U_NDP_CODE")
    private String U_NDP_CODE = "47810184-432e-4022-b90e-5e125bf7fcbe";
    @JsonProperty("U_CL_TSCHEDULE")
    private String U_CL_TSCHEDULE = "";
    @JsonProperty("U_NDP_MIG_EST")
    private String U_NDP_MIG_EST = "FIN";
    @JsonProperty("U_NDP_MIG_FEC")
    private String U_NDP_MIG_FEC = "2024-01-30";
    @JsonProperty("U_NDP_MIG_MSJ")
    private String U_NDP_MIG_MSJ = "Completed migration";
    @JsonProperty("U_CL_NOMALT")
    private String U_CL_NOMALT = "";
    @JsonProperty("U_NDP_TC")
    private double U_NDP_TC = 3.8;
    @JsonProperty("U_NDP_CLADOC")
    private String U_NDP_CLADOC = "0002";
    @JsonProperty("DocumentLines")
    private List<DocumentLine> DocumentLines;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(ProcesadorGeneral.class);

    public Factura(String id, String cliente, double monto) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
    }

    public Factura(String Serie_correlativo) {
        this.id = Serie_correlativo;
        DocumentLinesBinAllocation binAllocation = new DocumentLinesBinAllocation();
        LineTaxJurisdiction taxJurisdiction = new LineTaxJurisdiction();
        DocumentLine documentLine = new DocumentLine(List.of(binAllocation), List.of(taxJurisdiction));
        this.NumAtCard = Serie_correlativo;
        this.U_BPV_NCON2 = NumAtCard.split("-")[1];
        this.U_BPV_SERI = NumAtCard.substring(2, 6) + "-01";
        this.DocumentLines = List.of(documentLine);
    }

    @Getter
    @Setter
    public static class DocumentLine {
        @JsonProperty("DiscountPercent")
        private double DiscountPercent = 0.0;
        @JsonProperty("UnitPrice")
        private double UnitPrice = 93.135593;
        @JsonProperty("ItemCode")
        private String ItemCode = "101010185220AD258301T40";
        @JsonProperty("ItemDescription")
        private String ItemDescription = "AZALEIA CASSIA SOFT COMFY PAPETE-522 WUESO/NEGRO SINTETICO  T40";
        @JsonProperty("Quantity")
        private double Quantity = 1.0;
        @JsonProperty("MeasureUnit")
        private String MeasureUnit = "PAR";
        @JsonProperty("TaxCode")
        private String TaxCode = "I18";
        @JsonProperty("WarehouseCode")
        private String WarehouseCode = "08001";
        @JsonProperty("U_tipoOpT12")
        private String U_tipoOpT12 = "01";
        @JsonProperty("DocCurrency")
        private String DocCurrency = "SOL";
        @JsonProperty("U_BPP_OPER")
        private String U_BPP_OPER = "A";
        @JsonProperty("U_VS_ONEROSO")
        private String U_VS_ONEROSO = "1";
        @JsonProperty("U_VS_TIPAFE")
        private String U_VS_TIPAFE = "10";
        @JsonProperty("TaxOnly")
        private String TaxOnly = "tNO";
        @JsonProperty("CostingCode")
        private String CostingCode = "CCD1002";
        @JsonProperty("CostingCode2")
        private String CostingCode2 = "CCD2001";
        @JsonProperty("CostingCode3")
        private String CostingCode3 = "CCD3008";
        @JsonProperty("DocumentLinesBinAllocations")
        private List<DocumentLinesBinAllocation> DocumentLinesBinAllocations;
        @JsonProperty("LineTaxJurisdictions")
        private List<LineTaxJurisdiction> LineTaxJurisdictions;

        public DocumentLine(List<DocumentLinesBinAllocation> documentLinesBinAllocations, List<LineTaxJurisdiction> lineTaxJurisdictions) {
            this.DocumentLinesBinAllocations = documentLinesBinAllocations;
            this.LineTaxJurisdictions = lineTaxJurisdictions;
        }
    }

    @Getter
    @Setter
    public static class DocumentLinesBinAllocation {
        @JsonProperty("BinAbsEntry")
        private int BinAbsEntry = 25;
        @JsonProperty("Quantity")
        private double Quantity = 1.0;
    }

    @Getter
    @Setter
    public static class LineTaxJurisdiction {
        @JsonProperty("JurisdictionCode")
        private String JurisdictionCode = "I18";
        @JsonProperty("JurisdictionType")
        private int JurisdictionType = 8;
        @JsonProperty("ExternalCalcTaxRate")
        private double ExternalCalcTaxRate = 18.0;
        @JsonProperty("ExternalCalcTaxAmount")
        private double ExternalCalcTaxAmount = 16.76;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String getId() {
        return id;
    }

    public void procesar(SAPService sapService) {
        String threadName = Thread.currentThread().getName();
        String currentTime = LocalDateTime.now().format(formatter);
        try {
            sapService.postToSAP(this.toJson(), "Invoices");
        } catch (JsonProcessingException e) {
            logger.warn("Error procesando objeto ID: " + id + " - " + e.getMessage(), e);
        }

    }

    @Override
    public void procesar(NDPServices ndpServices) {

    }
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class StockTransfer implements Procesable {
    @Override
    public void procesar(NDPServices ndpServices) {
        ndpServices.ndpPost("core-engine/stock-transfer", this.getCadena(), Object.class);
    }

    private Object Cadena;
    private String code;
    @JsonProperty("detail")
    private List<DetailST> detail;
    @JsonProperty("businessPartnerCode")
    private String businessPartnerCode;
    @JsonProperty("nif")
    private String nif;
    @JsonProperty("cashRegisterCode")
    private String cashRegisterCode;
    @JsonProperty("receiptType")
    private String receiptType;
    @JsonProperty("businessPartnerName")
    private String businessPartnerName;
    @JsonProperty("typeTransfer")
    private String typeTransfer;
    @JsonProperty("storeDestination")
    private String storeDestination;
    @JsonProperty("storeOrigin")
    private String storeOrigin;
    @JsonProperty("storeOriginName")
    private String storeOriginName;
    @JsonProperty("storeDestinationName")
    private String storeDestinationName;
    @JsonProperty("document")
    private String document;
    @JsonProperty("process")
    private String process;
    @JsonProperty("statusOperation")
    private String statusOperation;
    @JsonProperty("createUser")
    private String createUser;
    @JsonProperty("createUserName")
    private String createUserName;
    @JsonProperty("createUserMail")
    private String createUserMail;
    @JsonProperty("salesPersonCode")
    private int salesPersonCode;
    @JsonProperty("createDate")
    private String createDate;
    @JsonProperty("transferDate")
    private String transferDate;
    @JsonProperty("transferReason")
    private String transferReason;
    @JsonProperty("isReturnTransfer")
    private String isReturnTransfer;
    @JsonProperty("totalGrossWeight")
    private double totalGrossWeight;
    @JsonProperty("channelApp")
    private String channelApp;
    @JsonProperty("cashRegister")
    private String cashRegister;
    @JsonProperty("warehouseCodeFrom")
    private String warehouseCodeFrom;
    @JsonProperty("warehouseCodeTo")
    private String warehouseCodeTo;
    @JsonProperty("warehouseFromName")
    private String warehouseFromName;
    @JsonProperty("warehouseToName")
    private String warehouseToName;
    @JsonProperty("ubigeoFrom")
    private String ubigeoFrom;
    @JsonProperty("startingAddress")
    private String startingAddress;
    @JsonProperty("startingLocation")
    private String startingLocation;
    @JsonProperty("ubigeoTo")
    private String ubigeoTo;
    @JsonProperty("arrivalAddress")
    private String arrivalAddress;
    @JsonProperty("arrivalLocation")
    private String arrivalLocation;
    @JsonProperty("carrierCompanyRUC")
    private String carrierCompanyRUC;
    @JsonProperty("carrierCompanyName")
    private String carrierCompanyName;
    @JsonProperty("carrierCompanyAddress")
    private String carrierCompanyAddress;
    @JsonProperty("vehiclePlate")
    private String vehiclePlate;
    @JsonProperty("vehicleBrand")
    private String vehicleBrand;
    @JsonProperty("transferModality")
    private String transferModality;
    @JsonProperty("carrierName")
    private String carrierName;
    @JsonProperty("carrierDocType")
    private String carrierDocType;
    @JsonProperty("carrierDoc")
    private String carrierDoc;
    @JsonProperty("driverLicense")
    private String driverLicense;
    @JsonProperty("carrierCompanyCode")
    private String carrierCompanyCode;
    @JsonProperty("warehouseCodeFromSAP")
    private String warehouseCodeFromSAP;
    @JsonProperty("warehouseCodeToSAP")
    private String warehouseCodeToSAP;
    @JsonProperty("transportDate")
    private String transportDate;

    @Override
    public String getId() {
        return "";
    }

    @Override
    public void procesar(SAPService sapService) {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailST {
        @JsonProperty("sunatOperation")
        private String sunatOperation;
        @JsonProperty("baseQuantity")
        private long baseQuantity;
        @JsonProperty("itemCode")
        private String itemCode;
        @JsonProperty("itemName")
        private String itemName;
        @JsonProperty("numberLine")
        private int numberLine;
        @JsonProperty("quantity")
        private int quantity;
        @JsonProperty("quantityIn")
        private int quantityIn;
        @JsonProperty("tipo")
        private String tipo;
        @JsonProperty("quantitySaleOrder")
        private int quantitySaleOrder;
        @JsonProperty("weight")
        private double weight;
        @JsonProperty("unitMsrCode")
        private String unitMsrCode;
        @JsonProperty("unitMSREntry")
        private int unitMSREntry;
        @JsonProperty("unitGroupEntry")
        private int unitGroupEntry;
        @JsonProperty("warehouseCodeFrom")
        private String warehouseCodeFrom;
        @JsonProperty("locationObjectFrom")
        private List<LocationObject> locationObjectFrom;
        @JsonProperty("locationFrom")
        private String locationFrom;
        @JsonProperty("locationEntryFrom")
        private int locationEntryFrom;
        @JsonProperty("locationEntryFromSAP")
        private int locationEntryFromSAP;
        @JsonProperty("warehouseCodeTo")
        private String warehouseCodeTo;
        @JsonProperty("storeDestination")
        private String storeDestination;
        @JsonProperty("locationObjectTo")
        private List<LocationObject> locationObjectTo;
        @JsonProperty("locationTo")
        private String locationTo;
        @JsonProperty("locationEntryTo")
        private int locationEntryTo;
        @JsonProperty("locationEntryToSAP")
        private int locationEntryToSAP;
        @JsonProperty("stockBatchList")
        private List<StockBatch> stockBatchList;
        @JsonProperty("batchCode")
        private String batchCode;
        @JsonProperty("systemNumber")
        private int systemNumber;
        @JsonProperty("curveDTO")
        private CurveDTO curveDTO;
        @JsonProperty("quantityPackage")
        private int quantityPackage;
        @JsonProperty("batchCurve")
        private String batchCurve;
        @JsonProperty("bardCode")
        private String bardCode;

        // Getters and Setters...
    }

    public static class LocationObject {
        @JsonProperty("createDate")
        private String createDate;
        @JsonProperty("updateDate")
        private String updateDate;
        @JsonProperty("status")
        private String status;
        @JsonProperty("companyCode")
        private String companyCode;
        @JsonProperty("itemCode")
        private String itemCode;
        @JsonProperty("warehouseCode")
        private String warehouseCode;
        @JsonProperty("locationEntry")
        private int locationEntry;
        @JsonProperty("locationCode")
        private String locationCode;
        @JsonProperty("locationType")
        private String locationType;
        @JsonProperty("quantity")
        private long quantity;
        @JsonProperty("type")
        private String type;

        // Getters and Setters
    }

    public static class StockBatch {
        @JsonProperty("createDate")
        private String createDate;
        @JsonProperty("updateDate")
        private String updateDate;
        @JsonProperty("status")
        private String status;
        @JsonProperty("companyCode")
        private String companyCode;
        @JsonProperty("itemCode")
        private String itemCode;
        @JsonProperty("warehouseCode")
        private String warehouseCode;
        @JsonProperty("batchCode")
        private String batchCode;
        @JsonProperty("systemNumber")
        private int systemNumber;
        @JsonProperty("quantity")
        private long quantity;
        @JsonProperty("curveSizesCode")
        private String curveSizesCode;
        @JsonProperty("itemName")
        private String itemName;
        @JsonProperty("stockBatchLocation")
        private List<Object> stockBatchLocation;
    }

    public static class CurveDTO {
        @JsonProperty("total")
        private int total;
        @JsonProperty("sizes")
        private List<Size> sizes;
        @JsonProperty("curveSizesCode")
        private String curveSizesCode;
        @JsonProperty("createDate")
        private String createDate;
    }

    public static class Size {
        @JsonProperty("quantity")
        private int quantity;
        @JsonProperty("size")
        private String size;

        // Getters and Setters
    }

}


// Clase Pago que implementa Procesable
class Pago implements Procesable {
    private String id;
    private String cliente;
    private double monto;

    public Pago(String id, String cliente, double monto) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
    }

    public String getId() {
        return id;
    }

    public void procesar(SAPService sapService) {
    }

    @Override
    public void procesar(NDPServices ndpServices) {
        //ndpServices.ndpPost("core-engine/stock-transfer", this, StockTransfer.class);

    }
}

// Clase NotaDeCredito que implementa Procesable
class NotaDeCredito implements Procesable {
    private String id;
    private String cliente;
    private double monto;

    public NotaDeCredito(String id, String cliente, double monto) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
    }

    public String getId() {
        return id;
    }

    public void procesar(SAPService sapService) {
        /*try {
            // Simulación de procesamiento
            Thread.sleep(500); //Simulamos el acceso a BD para obtener la data a enviar
            Thread.sleep(1500); // Simulamos 1 segundo de procesamiento
            System.out.println("Procesando nota de crédito ID: " + id + " de cliente: " + cliente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void procesar(NDPServices ndpServices) {

    }
}

class SAPService {
    private static final org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger(SAPService.class);
    String URLBase = "https://azaleia.sl.360salesolutions.com/b1s/v1/";
    Optional<String> cookie = Optional.empty();

    public SAPService() {
        cookie = loginToSAP();
    }

    public void postToSAP(String objetoJSON, String path) {
        try {
            String url = URLBase + path;
            logger.warn("Request: " + objetoJSON);

            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(120)).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").header("Cookie", cookie.get()).POST(HttpRequest.BodyPublishers.ofString(objetoJSON)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                logger.warn("Response" + response.body());
            } else {
                logger.warn("Response - Error" + response.body());
            }
        } catch (IOException | InterruptedException e) {
            logger.warn("Error durante la solicitud POST a SAP: " + e.getMessage(), e);
            int maxReintentos = 8;
            for (int i = 0; i < maxReintentos; i++) {
                try {
                    String url = URLBase + "/Invoices";
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(objetoJSON);
                    logger.warn("Request Catch: (" + i + ")" + objetoJSON);
                    HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(120)).build();
                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").header("Cookie", cookie.get()).POST(HttpRequest.BodyPublishers.ofString(json)).build();
                    // Reintentar la operación
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        logger.warn("Response Catch: (" + i + ")" + response.body());
                        break;  // Éxito en el reintento
                    }
                } catch (IOException | InterruptedException retryEx) {
                    logger.warn("Reintento fallido: " + (i + 1) + " - " + retryEx.getMessage(), retryEx);
                }
            }
        }
    }

    public static Optional<String> loginToSAP() {
        String URLBase = "https://azaleia.sl.360salesolutions.com/b1s/v1/";
        Optional<String> cookie = Optional.empty();
        try {
            String url = URLBase + "Login";
            String json = """
                        {
                            "CompanyDB": "B1H_AZALEIA_LOCALIZACION2",
                            "Password": "B1Admin$$",
                            "UserName": "manager"
                        }
                    """;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<String> cookies = response.headers().allValues("Set-Cookie");
                if (!cookies.isEmpty()) {
                    cookie = Optional.of(cookies.get(0));
                    logger.warn("Login Existoso: " + cookie.get());
                    return cookie;
                }
            } else {
                logger.warn("Login request failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            logger.warn("Error during login to SAP", e);
        }
        return Optional.empty();
    }
}