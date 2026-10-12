package com.guhecloud.cube.deliver.project.merchant.enums;

/**
 * 易耗品
 */

public enum ConsumablesEnum {

    BUSINESS_TYPE("consumables", "易耗品"),
    BUSINESS_TYPE_STOCK("consumablesStock", "易耗品出入库"),

    STATUS_NORMAL("consumablesStatus_normal", "正常"),
    STATUS_TOO_LOW("consumablesStatus_low", "过低"),
    STATUS_TOO_HIGH("consumablesStatus_high", "过高"),

    STOCK_TYPE_IN("consumablesStockType_in", "入库"),
    STOCK_TYPE_OUT("consumablesStockType_out", "出库"),

    IN_STOCK_TYPE("inStockType", "入库类型"),
    OUT_STOCK_TYPE("outStockType", "出库类型"),

    ;

    private String code;
    private String name;

    ConsumablesEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(String code) {
        for (ConsumablesEnum consumablesEnum : ConsumablesEnum.values()) {
            if (consumablesEnum.getCode().equals(code)) return consumablesEnum.getName();
        }
        return "";
    }
}
