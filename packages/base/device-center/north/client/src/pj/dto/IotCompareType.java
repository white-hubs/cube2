package com.guhecloud.cube.iot.north.bridge.client.pj.dto;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.iot.north.bridge.client.vo.IotDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持比较符类型
 */
public enum IotCompareType {

    EQ(1, "EQ", "等于"),
    NE(1, "NE", "不等于"),
    LT(1, "LT", "小于"),
    LE(1, "LE", "小于等于"),
    GT(1, "GT", "大于"),
    GE(1, "GE", "大于等于"),
    BETWEEN(2, "BETWEEN", "介于"),
    NOT_BETWEEN(2, "NOT_BETWEEN", "不介于"),
    IN(-1, "IN", "包含"),
    NOT_IN(-1, "NOT_IN", "不包含"),
    /* 以下用于String, 目前尚未使用 */
    BIT_SET(1, "BIT_SET", "位运算"),
    START_WITH(1, "START_WITH", "以...开始"),
    END_WITH(1, "END_WITH", "以...结束"),
    LIKE(1, "LIKE", "包含"),
    CONTAINS(1, "CONTAINS", "包含");

    public final int paramCount;
    public final String code;
    public final String label;

    public static final String DATA_TYPE = "dataType";
    public static final String DATA_TYPE_NAME = "dataTypeName";
    public static final String OPERATORS = "operators";
    public static final String OPERATOR_CODE = "operatorCode";
    public static final String OPERATOR_LABEL = "operatorLabel";
    public static final String PARAM_COUNT = "paramCount";

    IotCompareType(int paramCount, String code, String label) {
        this.paramCount = paramCount;
        this.code = code;
        this.label = label;
    }

    public static IotCompareType fromCode(String code) {
        for (IotCompareType val : values()) {
            if (val.name().equalsIgnoreCase(code)) {
                return val;
            }
        }
        return null;
    }

    public int getParamCount() {
        return paramCount;
    }

    private static final List<IotCompareType> BOOL_OPERATORS = ImmutableList.of(NE, EQ);
    private static final List<IotCompareType> INT_OPERATORS = ImmutableList.of(NE, EQ, LE, LT, GT, GE, IN, NOT_IN, BETWEEN);
    private static final List<IotCompareType> ENUM_OPERATORS = ImmutableList.of(NE, EQ, IN, NOT_IN);
    // 浮点数是否需要有非高精度的NE, EQ, IN?
    private static final List<IotCompareType> FLOAT_OPERATORS = ImmutableList.of(LE, LT, GT, GE, BETWEEN);
    private static final List<IotCompareType> STRING_OPERATORS = ImmutableList.of(NE, EQ, START_WITH, END_WITH, CONTAINS);

    private static final Map<IotDataType, List<IotCompareType>> operatorByValueType =
            ImmutableMap.<IotDataType, List<IotCompareType>>builder()
                    .put(IotDataType.BOOLEAN, BOOL_OPERATORS)
                    .put(IotDataType.DOUBLE, FLOAT_OPERATORS)
                    .put(IotDataType.FLOAT, FLOAT_OPERATORS)
                    .put(IotDataType.INT, INT_OPERATORS)
                    .put(IotDataType.ENUM, ENUM_OPERATORS)
                    .put(IotDataType.STRING, STRING_OPERATORS)
                    .build();

    private static final Map<IotDataType, List<IotCompareType>> valueTypeWithOperatorTypes =
            ImmutableMap.<IotDataType, List<IotCompareType>>builder()
                    .put(IotDataType.BOOLEAN, ImmutableList.of(NE, EQ))
                    .put(IotDataType.DOUBLE, ImmutableList.of(NE, EQ, LE, LT, GT, GE, IN, NOT_IN))
                    .put(IotDataType.FLOAT, ImmutableList.of(NE, EQ, LE, LT, GT, GE, IN, NOT_IN))
                    .put(IotDataType.INT, ImmutableList.of(NE, EQ, LE, LT, GT, GE, IN, NOT_IN))
                    .put(IotDataType.ENUM, ImmutableList.of(NE, EQ, IN, NOT_IN))
                    .put(IotDataType.STRING, ImmutableList.of(NE, EQ))
                    .build();

    public static List<Map<String, Object>> supportedvalueTypeWithOperatorTypes() {
        List<Map<String, Object>> supportedData = new ArrayList<>();

        valueTypeWithOperatorTypes.forEach((iotDataType, operators) -> {
            Map<String, Object> one = new HashMap<>();
            one.put(DATA_TYPE, iotDataType.getCode());
            one.put(DATA_TYPE_NAME, iotDataType.getLabel());

            List<Map<String, Object>> operatorsList = new ArrayList<>();
            for (IotCompareType operator : operators) {
                Map<String, Object> oneOperator = new HashMap<>();
                oneOperator.put(OPERATOR_CODE, operator.code.toLowerCase());
                oneOperator.put(OPERATOR_LABEL, operator.label);
                oneOperator.put(PARAM_COUNT, operator.paramCount);
                operatorsList.add(oneOperator);
            }

            one.put(OPERATORS, operatorsList);
            supportedData.add(one);
        });

        return supportedData;
    }

    public static final List<IotCompareType> SUPPORT_OPERATORS_NOW = ImmutableList.of(EQ, NE, LT, LE, GT, GE);

    public static List<IotCompareType> supportedOperators(IotDataType IotDataType) {
        return operatorByValueType.getOrDefault(IotDataType, Collections.emptyList());
    }
}
