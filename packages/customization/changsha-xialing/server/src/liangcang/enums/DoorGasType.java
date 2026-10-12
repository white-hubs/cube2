package com.guhecloud.cube.customization.xialing.server.liangcang.enums;

/** 门禁联动支持的气体类型。 */
public enum DoorGasType {
    /** 氧气。 */
    O2("氧气", "o2"),
    /** 二氧化碳。 */
    CO2("二氧化碳", "co2"),
    /** 磷化氢。 */
    PH3("磷化氢", "ph3");

    private final String chineseName;
    private final String englishName;

    DoorGasType(String chineseName, String englishName) {
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    public String chineseName() {
        return chineseName;
    }

    public String englishName() {
        return englishName;
    }
}
