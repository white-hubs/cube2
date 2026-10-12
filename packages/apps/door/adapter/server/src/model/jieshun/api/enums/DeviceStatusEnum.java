package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 设备状态枚举
 * 对应数据字典值
 */
@AllArgsConstructor
@Getter
public enum DeviceStatusEnum {

    /**
     * 01 在线
     * 02 离线
     * 03 开
     * 04 关
     * 05 报警
     * 06 开+报警
     * 07 关+报警
     * 51 不支持
     * 52 未知
     * 53 高峰状态
     * 54 托管状态
     */
    ONLINE("01", "在线"),
    OFFLINE("02", "离线"),
    OPEN("03", "开"),
    CLOSE("04", "关"),
    ALARM("05", "报警"),
    OPEN_ALARM("06", "开+报警"),
    CLOSE_ALARM("07", "关+报警"),
    NOT_SUPPORT("51", "不支持"),
    UNKNOWN("52", "未知"),
    HIGH_FLOW("53", "高峰状态"),
    TRUST("54", "托管状态");

    private final String code;
    private final String desc;

    public static String getDescBy(String code) {
        for (DeviceStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        throw new IllegalArgumentException("设备状态枚举不存在");
    }

    /**
     * switch (doorState){
     * //常开
     * case 0:
     * return 0;
     * //关闭
     * case 1:
     * return 2;
     * //开启
     * case 2:
     * return 1;
     * //常关
     * case 3:
     * return 3;
     * default:
     * return doorState;
     * }
     */
    @NonNull
    public static Integer getReturnStatus(String code) {
        switch (code) {
            case "03":
                return 1;
            case "04":
                return 2;
            default:
                return Integer.parseInt(code);
        }
    }
}