package com.guhecloud.cube.base.device.center.metadata.util;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import com.guhecloud.cube.base.device.center.metadata.lang.DataTypeEnum;
import com.guhecloud.platform.base.util.StringUtils;

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
    public static AttributePermissionEnum convertReadWriteEnum(int iotVal) {
        return AttributePermissionEnum.convertBySourceVal(iotVal);
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
    public static String iotPropVal2Chinese(String dataType, final String iotPropVal, JSONArray dataConstraint) {
        DataTypeEnum dataTypeEnum = DataTypeEnum.convertByCode(dataType);

        if (Objects.isNull(dataConstraint) || dataConstraint.isEmpty()) {
            throw new IllegalArgumentException("dataConstraint is null or empty");
        }

        if (StringUtils.isBlank(iotPropVal)) {
            return "";
        }

        //[{"value":0,"label":"yes"},{"value":1,"label":"no"}]
        switch (dataTypeEnum) {
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
                return Objects.isNull(iotPropVal) ? null : iotPropVal;
            case BOOLEAN:
            case ENUM:
                for (Object o : dataConstraint) {
                    Map item = (Map) o;
                    if (iotPropVal.equals(String.valueOf(item.get("value")))) {
                        return String.valueOf(item.get("label"));
                    }
                }
                throw new IllegalArgumentException("iotPropVal=" + iotPropVal + " is illegal");
            default:
                throw new IllegalArgumentException("unsupported dataType=" + dataType);
        }
    }

}
