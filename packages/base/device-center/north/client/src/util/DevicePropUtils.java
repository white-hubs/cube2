package com.guhecloud.cube.iot.north.bridge.client.util;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.iot.north.bridge.client.vo.IotDataType;
import com.guhecloud.cube.iot.north.bridge.client.vo.ReadWriteEnum;

import java.util.Map;
import java.util.Objects;

public class DevicePropUtils {
    private DevicePropUtils() {
    }

    /**
     * 根据iotVal获取枚举
     *
     * @param iotVal 源值
     *
     * @return 枚举
     */
    public static ReadWriteEnum convertReadWriteEnum(int iotVal) {
        return ReadWriteEnum.convertBySourceVal(iotVal);
    }

    /**
     * 将值转换为中文<br/>
     * 例如：iotPropVal=0, dataType=boolean, dataConstraint=[{"value":0,"label":"yes"},{"value":1,"label":"no"}]<br/>
     * 返回：yes
     *
     * @param dataType       数据类型
     * @param iotPropVal     iot值
     * @param dataConstraint 数据约束
     *
     * @return 中文
     */
    public static String iotPropVal2Chinese(String dataType, final Object iotPropVal, JSONArray dataConstraint) {
        IotDataType iotDataType = IotDataType.convertByCode(dataType);

        //[{"value":0,"label":"yes"},{"value":1,"label":"no"}]
        switch (iotDataType) {
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
                return String.valueOf(iotPropVal);
            case BOOLEAN:
            case ENUM:
                return getEnumValue(iotPropVal, dataConstraint);
            default:
                throw new IllegalArgumentException("unsupported dataType=" + dataType);
        }
    }

    private static String getEnumValue(Object iotPropVal, JSONArray dataConstraint) {
        if (Objects.isNull(dataConstraint) || dataConstraint.isEmpty()) {
            throw new IllegalArgumentException("dataConstraint is null or empty");
        }

        for (Object o : dataConstraint) {
            Map item = (Map) o;
            if (iotPropVal.equals(String.valueOf(item.get("value")))) {
                return String.valueOf(item.get("label"));
            }
        }
        throw new IllegalArgumentException("iotPropVal=" + iotPropVal + " is illegal");
    }

    /**
     * 在线转换为中文<br/>
     *
     * @param onlineState 在线状态
     *
     * @return 中文
     */
    public static String onlineState2Chinese(int onlineState) {
        switch (onlineState) {
            case 0:
                return "离线";
            case 1:
                return "在线";
            default:
                throw new IllegalArgumentException("onlineState=" + onlineState + " is illegal");
        }
    }
}
