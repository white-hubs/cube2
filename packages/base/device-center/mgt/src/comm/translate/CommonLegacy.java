package com.guhecloud.cube.iot.mgt.comm.translate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Objects;

public class CommonLegacy {

    /**
     * @param dataConfig
     * @param dataType
     * @param presentValue
     *
     * @return
     */
    public static Object toLabelName(String dataConfig, String dataType, Object presentValue) {
        if (Objects.nonNull(presentValue)) {
            presentValue = presentValue.toString().replace("\"", "");
        }
        Object value = getValueObject(dataType, presentValue);
        if (IotDataType.ENUM.getCode().equals(dataType)
            || IotDataType.BOOLEAN.getCode().equals(dataType)) {
            JSONArray jsonArray = JSON.parseArray(dataConfig);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("value").equals(value.toString())) {
                    value = jsonObject.get("label");
                }
            }
        }
        return value;
    }

    public static Object getValueObject(String dataType, Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        String value = obj.toString();
        if (StringUtils.isBlank(value)) {
            value = value.replace("\"", "");
        }
        if (IotDataType.INT.getCode().equals(dataType)) {
            return Float.valueOf(value).intValue();
        } else if (IotDataType.LONG.getCode().equals(dataType)) {
            return Long.parseLong(value);
        } else if (IotDataType.FLOAT.getCode().equals(dataType)) {
            return Float.parseFloat(value);
        } else if (IotDataType.DOUBLE.getCode().equals(dataType)) {
            return Double.parseDouble(value);
        } else if (IotDataType.BOOLEAN.getCode().equals(dataType) ||IotDataType.ENUM.getCode().equals(dataType)
            || IotDataType.STRING.getCode().equals(dataType)) {
            return String.valueOf(value);
        }else {
            return value;
        }
    }
}
