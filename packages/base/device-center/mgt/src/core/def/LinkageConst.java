package com.guhecloud.cube.iot.mgt.core.def;

import com.guhecloud.platform.base.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class LinkageConst {
    public static final String LINKAGE_EXPRESS = "iot:link:expr:";
    public static final String LINKAGE_MATE = "iot:link:mate:";
    public static final String LINKAGE_RESULT = "iot:link:result:";

    public static String buildSingleMateLink(String deviceId, String propCode) {
        return "d_" + deviceId + "p_" + propCode;
    }

    /**
     * 构建单个联动表达式<br/>
     * 在有些情况下当个属性会多次出现在联动中
     *
     * @param deviceId   设备id
     * @param propCode   属性id
     * @param compare    比较类型
     * @param compareVal 比较值
     *
     * @return 联动表达式
     */
    public static String buildSingleLinkExpress(String deviceId, String propCode, String compare, String compareVal) {
        String key = "d_" + deviceId + "p_" + propCode + "c_" + compare + "v_" + compareVal;
        return DigestUtils.md5Hex(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 功能描述: obj数组转Str表述
     *
     * @param values values
     *
     * @return java.lang.String
     *
     * @auther: fl
     * @date: 2022/11/26 18:57
     */
    public static String compareVal2Str(Object[] values) {
        if (values.length < 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Object val : values) {
            sb.append(val);
        }

        return sb.toString();
    }

}
