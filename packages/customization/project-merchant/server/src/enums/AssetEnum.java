package com.guhecloud.cube.deliver.project.merchant.enums;

/**
 * 资产
 */

public enum AssetEnum {

    BUSINESS_TYPE("asset", "资产"),
    BUSINESS_TYPE_ALLOCATION("assetAllocation", "资产调拨"),

    STATUS("assetStatus","使用状态"),
    STATUS_FREE("assetStatus_free", "闲置"),
    STATUS_USE("assetStatus_use", "使用"),
    STATUS_HANDLED("assetStatus_handled", "已处置"),
    STATUS_LOSE("assetStatus_lose", "已遗失"),


    SOURCE("assetSource","资产来源"),
    SOURCE_BUILD("assetSource_build", "自建"),
    SOURCE_BUY("assetSource_buy", "购买"),
    SOURCE_LEASE("assetSource_lease", "租赁"),

    // 盘点范围
    STOCKING_RANGE_ALL("assetStockingRange_all", "所有"),
    STOCKING_RANGE_TYPE("assetStockingRange_type", "指定类型"),
    STOCKING_RANGE_DEPT("assetStockingRange_dept", "指定部门"),
    STOCKING_RANGE_AREA("assetStockingRange_area", "指定区域"),

    // 盘点状态
    STOCKING_STATUS_NO_START("assetStockingStatus_noStart", "未盘点"),
    STOCKING_STATUS_PROGRESS("assetStockingStatus_progress", "盘点中"),
    STOCKING_STATUS_END("assetStockingStatus_end", "已盘点"),

    // 盘点结果
    STOCKING_RESULT_NORMAL("assetStockingResult_normal", "正常"),
    STOCKING_RESULT_WANE("assetStockingResult_wane", "盘亏")

    ;

    private String code;
    private String name;

    AssetEnum(String code, String name) {
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
        for (AssetEnum assetEnum : AssetEnum.values()) {
            if (assetEnum.getCode().equals(code)) return assetEnum.getName();
        }
        return "";
    }
}
