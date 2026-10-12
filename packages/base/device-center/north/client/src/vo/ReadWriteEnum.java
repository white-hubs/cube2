package com.guhecloud.cube.iot.north.bridge.client.vo;

/**
 * 读写枚举定义
 */
public enum ReadWriteEnum {
    /**
     * 只读
     */
    READ(0, "只读"),
    /**
     * 读写
     */
    READ_WRITE(1, "读写");

    public final int sourceVal;
    public final String chineseName;

    ReadWriteEnum(int sourceVal, String chineseName) {
        this.sourceVal = sourceVal;
        this.chineseName = chineseName;
    }


    /**
     * 根据sourceVal获取枚举
     *
     * @param sourceVal 源值
     * @return 枚举
     */
    public static ReadWriteEnum convertBySourceVal(int sourceVal) {
        for (ReadWriteEnum readWriteEnum : ReadWriteEnum.values()) {
            if (readWriteEnum.sourceVal == sourceVal) {
                return readWriteEnum;
            }
        }

        throw new IllegalArgumentException("sourceVal=" + sourceVal + " is illegal");
    }
}
