package com.guhecloud.cube.iot.mgt.comm.translate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

public class CfgConvertUtil {
    /**
     * 属性约束配置解析<br/>
     * 目前值解析为:["10","50"] 这类形式
     *
     * @param configJson  configJson
     * @param IotDataType dataType
     *
     * @return java.lang.String
     *
     * @author Fl
     **/
    public static String propDataConfigCheckOrDefault(String configJson, IotDataType IotDataType) {
        //        Assert.isBlank(configJson, "属性配置不能为空");
        Assert.isTrue(!Objects.isNull(IotDataType), "数据类型不能为空");

        //数据约束
        JSONArray constraints = JSON.parseArray(configJson);

        String paramStr = "[]";
        switch (IotDataType) {
            case FLOAT:
                if (nonNull(constraints)) {
                    Assert.isTrue(constraints.size() == 2, "范围约束错误,示例[1.0, 99.2]");
                    float begin = constraints.getFloatValue(0);
                    float end = constraints.getFloatValue(1);
                    paramStr = "[" + begin + "," + end + "]";
                } else {
                    paramStr = "[" + Float.MIN_VALUE + "," + Float.MIN_VALUE + "]";
                }
                break;
            case DOUBLE:
                if (nonNull(constraints)) {
                    Assert.isTrue(constraints.size() == 2, "范围约束错误,示例[1.0, 99.2]");
                    double begin = constraints.getDoubleValue(0);
                    double end = constraints.getDoubleValue(1);
                    paramStr = "[" + begin + "," + end + "]";
                } else {
                    paramStr = "[" + Double.MIN_VALUE + "," + Double.MIN_VALUE + "]";
                }
                break;
            case INT:
                if (nonNull(constraints)) {
                    Assert.isTrue(constraints.size() == 2, "范围约束错误,示例[1,99]");
                    int begin = constraints.getIntValue(0);
                    int end = constraints.getIntValue(1);
                    paramStr = "[" + begin + "," + end + "]";
                } else {
                    paramStr = "[" + Integer.MIN_VALUE + "," + Integer.MIN_VALUE + "]";
                }
                break;
            case LONG:
                if (nonNull(constraints)) {
                    Assert.isTrue(constraints.size() == 2, "范围约束错误,示例[1,99]");
                    long begin = constraints.getLongValue(0);
                    long end = constraints.getLongValue(1);
                    paramStr = "[" + begin + "," + end + "]";
                } else {
                    paramStr = "[" + Long.MIN_VALUE + "," + Long.MIN_VALUE + "]";
                }
                break;
            case BOOLEAN:
            case ENUM:
                //枚举和boolean不能为空，必须定义约束类型
                if (nonNull(constraints)) {
                    List<KeyVal> keyValList = constraints.toJavaList(KeyVal.class);
                    Assert.notEmpty(keyValList, "值定义错误,示例[{\"value\":\"0\",\"label\":\"低档\"},{\"value\":\"1\",\"label\":\"中档\"},{\"value\":\"2\",\"label\":\"高档\"}]");

                    for (KeyVal keyVal : keyValList) {
                        Assert.isTrue(StringUtils.isNotBlank(keyVal.getLabel()) && StringUtils.isNotBlank(keyVal.getValue()),
                                      "值定义错误,示例[{\"value\":\"0\",\"label\":\"低档\"},{\"value\":\"1\",\"label\":\"中档\"},{\"value\":\"2\",\"label\":\"高档\"}]");
                    }

                    paramStr = configJson;
                }

                break;
            case STRING:
                //忽略检查
                break;
        }
        return paramStr;
    }

    @Data
    static class KeyVal {
        private String label;
        private String value;
    }
}
