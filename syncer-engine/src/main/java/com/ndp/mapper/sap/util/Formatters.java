package com.ndp.mapper.sap.util;

import java.nio.charset.StandardCharsets;
import java.text.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Formatters {
    String DATE_FORMAT_NDP = "yyyy-MM-dd";
    String DATE_TIME_FORMAT_NDP = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_SAP = "yyyy-MM-dd";
    private Formatters() {
    }

    public static String formatFromISOToUtf8(String value) {
        return new String(value.getBytes(), StandardCharsets.UTF_8);
    }

    public static String formatNumberAddLeadingZeros(Integer numberToFormat, int numberOfDigits) {
        String format = "%0" + numberOfDigits + "d";
        return String.format(format, numberToFormat);
    }

    public static String formatNumberAddLeadingZeros(String numberToFormatStr, int numberOfDigits) {
        Integer parsedNumberToFormat = Integer.valueOf(numberToFormatStr);
        String format = "%0" + numberOfDigits + "d";
        return String.format(format, parsedNumberToFormat);
    }

    public String formatDateToNDPString(Date date) {
        return date != null ?
                formatDateAsStringByPattern(date, this.DATE_FORMAT_NDP) : null;
    }
    /*
    public String formatSAPStringDateToNDPStringDateTime(String date) {
        Date instancedDate = formatSAPStringDateToDateInstance(date); //GMT
        if (instancedDate != null) {
            instancedDate = getDateWithTimeZone(instancedDate);
        }
        return instancedDate != null ?
                formatDateAsStringByPattern(instancedDate, this.DATE_TIME_FORMAT_NDP) : null;
    }*/

    public String formatNDPStringDateToSAPStringDate(String date) {
        try {
            DateFormat ndpDateTimeFormat = new SimpleDateFormat(this.DATE_FORMAT_NDP);
            Date parsedDate = ndpDateTimeFormat.parse(date);

            return formatDateToSAPString(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String formatNDPStringDateTimeToSAPStringDate(String date) {
        try {
            DateFormat ndpDateTimeFormat = new SimpleDateFormat(this.DATE_TIME_FORMAT_NDP);
            Date parsedDate = ndpDateTimeFormat.parse(date);

            return formatDateToSAPString(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String formatDateToSAPString(Date date_) {
        Date date = null;
        if (date_ != null) {
            date = getDateWithTimeZone(date_);
        }
        return date != null ?
                formatDateAsStringByPattern(date, DATE_FORMAT_SAP) : null;
    }

    /*public Date formatSAPStringDateToDateInstance(String date) {
        return Mappers.mapStringToDateInFormat(date, this.DATE_FORMAT_SAP);
    }*/

    public static String formatDateAsStringByPattern(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public String formatNdpStringDateForSAP_XSJS(String dateString) {
        //System.out.println("formatNdpStringDateForSAP_XSJS");
        try {
            DateFormat xsjsDateFormat = new SimpleDateFormat(this.DATE_TIME_FORMAT_NDP);
            Date parsedDate = xsjsDateFormat.parse(dateString);

            DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String formatServiceStringDateForQueryParam_SAP_XSJS(String dateString) {
        //System.out.println("formatServiceStringDateForQueryParam_SAP_XSJS");
        try {
            DateFormat xsjsDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date parsedDate = xsjsDateFormat.parse(dateString);

            DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Double formatDoubleToSixDecimal(Double number) {
        Locale currentLocale = Locale.getDefault();

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        // otherSymbols.setDecimalSeparator(',');
        otherSymbols.setDecimalSeparator('.');
        return Double.valueOf(new DecimalFormat("0.000000", otherSymbols).format(number));
    }

    private static Date getDateWithTimeZone(Date date) {
        //System.out.println("DATABASE: " + date);
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(),
                TimeZone.getTimeZone("GMT").toZoneId());
        //System.out.println("LDT GMT " + ldt);
        if (ldt.getHour() < 5) {
            date = Date.from(ldt.minusHours(5L).atZone(TimeZone.getTimeZone("GMT").toZoneId()).toInstant());
        } else {
            date = Date.from(ldt.atZone(TimeZone.getTimeZone("GMT").toZoneId()).toInstant());
        }
        //System.out.println("LIMA: " + date);
        //System.out.println("*************************************************");
        return date;
    }

    public static Date getCurrentGMT() {
        LocalDateTime ldt = LocalDateTime.now(TimeZone.getTimeZone("GMT").toZoneId());
        //System.out.println("LDT: " + ldt);
        //System.out.println("LDT-5: " + Date.from(ldt.minusHours(5L).atZone(TimeZone.getTimeZone("GMT").toZoneId()).toInstant()));
        return Date.from(ldt.minusHours(5L).atZone(TimeZone.getTimeZone("GMT").toZoneId()).toInstant());
    }

    public static String getTransactionalCode() {
        LocalDateTime ldt = LocalDateTime.now(TimeZone.getTimeZone("GMT").toZoneId());
        StringBuilder sTransactionCode = new StringBuilder();
        sTransactionCode.append(ldt.getYear());
        //month
        if (ldt.getMonthValue() < 10) {
            sTransactionCode.append("0");
        }
        sTransactionCode.append(ldt.getMonthValue());
        //day
        if (ldt.getDayOfMonth() < 10) {
            sTransactionCode.append("0");
        }
        sTransactionCode.append(ldt.getDayOfMonth());
        //hour
        if (ldt.getHour() < 10) {
            sTransactionCode.append("0");
        }
        sTransactionCode.append(ldt.getHour());
        //minute
        if (ldt.getMinute() < 10) {
            sTransactionCode.append("0");
        }
        sTransactionCode.append(ldt.getMinute());
        //second
        if (ldt.getSecond() < 10) {
            sTransactionCode.append("0");
        }
        sTransactionCode.append(ldt.getSecond());
        return sTransactionCode.toString();
    }

    private static String convertStringDateGMTToStringDateLima(String date, String formatBuilder) {
        String d = null;
        //System.out.println("ORIGINAL: " + date);
        DateFormat format = new SimpleDateFormat(formatBuilder);
        try {
            Date parsedDate = format.parse(date);
            LocalDateTime ldt = LocalDateTime.ofInstant(parsedDate.toInstant(),
                    TimeZone.getTimeZone("GMT").toZoneId());
            Date newDate;
            if (ldt.getHour() >= 0) {
                newDate = Date.from(ldt.minusHours(5).atZone(
                        TimeZone.getTimeZone("GMT").toZoneId()).toInstant());
            } else {
                newDate = Date.from(ldt.atZone(
                        //TimeZone.getTimeZone("America/Lima").toZoneId()).toInstant());
                        TimeZone.getTimeZone("GMT").toZoneId()).toInstant());
            }
            d = format.format(newDate);
            //System.out.println("NEW ORIGINAL: " + d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String convertStringDateGMTToStringDateLima(String date) {
        return convertStringDateGMTToStringDateLima(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String convertStringOnlyDateGMTToStringDateLima(String date) {
        return convertStringDateGMTToStringDateLima(date, "yyyy-MM-dd");
    }


    public static void main(String[] args) {
    /*    //System.out.println(convertStringDateGMTToStringDateLima("2021-08-03 21:59:43"));
        System.out.println(convertStringDateGMTToStringDateLima("2021-11-16 00:30:16"));
        System.out.println(convertStringDateGMTToStringDateLima("2021-11-16", "yyyy-MM-dd"));
        System.out.println(getDateWithTimeZone(new Date()));
        System.out.println(getTransactionalCode());
*/

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
// Aqui usamos la instancia formatter para darle el formato a la fecha. Es importante ver que el resultado es un string.

        // System.out.println(formatter.format(new Date()));
    }
}
