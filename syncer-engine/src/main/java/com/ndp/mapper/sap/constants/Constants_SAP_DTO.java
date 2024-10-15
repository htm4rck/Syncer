package com.ndp.mapper.sap.constants;

import com.ndp.mapper.ndp.constants.Constants_NDP;

import java.util.Objects;

public interface Constants_SAP_DTO {

    String MIGRATION_STATUS_PENDING = "PEN";
    String MIGRATION_STATUS_FINALIZED = "FIN";
    String MIGRATION_STATUS_ERROR = "ERR";
    String MIGRATION_STATUS_NULLIFIED = "ANU";
    String MIGRATION_STATUS_CLOSED = "CER";

    String MIGRATION_YES = "Y";
    String MIGRATION_NO = "N";

    String MIGRATION_MESSAGE_PENDING = "Pending migration";
    String MIGRATION_MESSAGE_COMPLETED = "Completed migration";
    String MIGRATION_MESSAGE_FAILED = "Failed migration";

    String B1S_YES = "tYES";
    String B1S_NO = "tNO";

    static String mapSapFlagToNdp(String sapFlag) {
        if (Objects.isNull(sapFlag) || sapFlag.trim().equals("")) return null;

        return B1S_YES.equals(sapFlag) ? Constants_NDP.NDP_YES : Constants_NDP.NDP_NO;
    }

    static Integer mapSapFlagToNdpInt(String sapFlag) {
        if (Objects.isNull(sapFlag) || sapFlag.trim().equals("")) return null;

        return B1S_YES.equals(sapFlag) ? Constants_NDP.NDP_YES_INT : Constants_NDP.NDP_NO_INT;
    }

    static String mapNdpFlagToSapFlag(Integer ndpFlag) {
        if (Objects.isNull(ndpFlag)) return null;

        return Constants_NDP.NDP_YES_INT.equals(ndpFlag) ? B1S_YES : B1S_NO;
    }

    static String mapNdpFlagToSapFlag(String ndpFlag) {
        if (Objects.isNull(ndpFlag)) return null;

        return Constants_NDP.NDP_YES.equals(ndpFlag) ? B1S_YES : B1S_NO;
    }

}
