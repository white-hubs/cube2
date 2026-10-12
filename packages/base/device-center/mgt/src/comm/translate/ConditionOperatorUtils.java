package com.guhecloud.cube.iot.mgt.comm.translate;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConditionOperatorUtils {

    /**
     * 将表达式因子转化为非技术人员可理解语句。
     */
    public static String toTransparentStatement(String compareType, Object[] compareValue,
                                                String dataConfig, String dataType
                                               ) {
        String transparentStatement;
        switch (compareType.toUpperCase()) {
            case "EQ": {
                transparentStatement = "当前值 == " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "LT": {
                transparentStatement = "当前值 < " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "LE": {
                transparentStatement = "当前值 <= " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "GT": {
                transparentStatement = "当前值 > " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "GE": {
                transparentStatement = "当前值 >= " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "NOT_IN": {
                List<Object> objectList = new ArrayList<>();
                for (int i = 0; i < compareValue.length - 1; i++) {
                    objectList.add(CommonLegacy.toLabelName(dataConfig, dataType, compareValue[i]));
                }
                transparentStatement = " 当前值 not in " + StringUtils.join(";", objectList);
                break;
            }
            case "IN": {
                List<Object> objectList = new ArrayList<>();
                for (int i = 0; i < compareValue.length - 1; i++) {
                    objectList.add(CommonLegacy.toLabelName(dataConfig, dataType, compareValue[i]));
                }
                transparentStatement = "当前值 in " + StringUtils.join(";", objectList);
                break;
            }
            case "BETWEEN": {
                transparentStatement =
                        "当前值 between " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]) + " and "
                        + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[1]);
                break;
            }
            case "NOT_BETWEEN": {
                transparentStatement =
                        "当前值 不在 " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]) + " 和 "
                        + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[1]) + "之间";
                break;
            }
            case "NE": {
                transparentStatement = "当前值 != " + CommonLegacy.toLabelName(dataConfig, dataType, compareValue[0]);
                break;
            }
            case "BIT_SET": {
                transparentStatement = "当前 第[" + compareValue[0] + "] 位为1";
                break;
            }
            default: {
                transparentStatement = "未知操作:" + compareType;
                break;
            }
        }
        return transparentStatement;
    }
}
