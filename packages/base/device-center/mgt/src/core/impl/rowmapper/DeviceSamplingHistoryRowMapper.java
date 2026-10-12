package com.guhecloud.cube.iot.mgt.core.impl.rowmapper;

import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryResultVo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DeviceSamplingHistoryRowMapper implements RowMapper<DeviceSamplingHistoryResultVo> {
    @Override
    public DeviceSamplingHistoryResultVo mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, DevicePropHistoryResultVo> resultMap = new HashMap<>();
        Timestamp ts = rs.getTimestamp("ts");
        String date = rs.getString("date");
        String[] propCode = toStringArray(rs.getArray("propCode"));
        String[] propDataType = toStringArray(rs.getArray("prop_data_type"));
        long[] intValue = toLongArray(rs.getArray("int_value"));
        int[] lcintValue = toIntArray(rs.getArray("lcint_value"));
        String[] stringValue = toStringArray(rs.getArray("string_value"));
        String[] translateValue = toStringArray(rs.getArray("translate_value"));
        for (int i = 0; i < propCode.length; i++) {
            if (resultMap.containsKey(propCode[i])) {
                continue;
            }
            DevicePropHistoryResultVo resultVO = new DevicePropHistoryResultVo();
            resultVO.setPropCode(propCode[i]);
            resultVO.setDate(date);
            resultVO.setTs(ts.getTime());
            if (propDataType.length > i) {
                IotDataType dataTypeEnum;
                try {
                    dataTypeEnum = IotDataType.fromCode(propDataType[i]);
                } catch (IllegalArgumentException e) {
                    dataTypeEnum = null;
                }

                if (dataTypeEnum != null) {
                    switch (dataTypeEnum) {
                        case INT:
                        case LONG:
                            if (intValue.length > i) {
                                resultVO.setValue(intValue[i]);
                            }
                            break;
                        case ENUM:
                        case BOOLEAN:
                            if (lcintValue.length > i) {
                                resultVO.setValue(lcintValue[i]);
                            }
                            break;
                        default:
                            if (stringValue.length > i) {
                                resultVO.setValue(stringValue[i]);
                            }
                    }
                } else if (stringValue.length > i) {
                    resultVO.setValue(stringValue[i]);
                }
            }
            if (stringValue.length > i) {
                resultVO.setValueOfStr(stringValue[i]);
            }
            if (translateValue.length > i) {
                resultVO.setValueOfTranslate(translateValue[i]);
            }
            resultMap.put(propCode[i], resultVO);
        }
        return DeviceSamplingHistoryResultVo.builder()
                .propValues(resultMap)
                .ts(ts.getTime())
                .build();
    }

    /**
     * 将 SQL Array 转换为 String 数组
     */
    private String[] toStringArray(Array array) throws SQLException {
        if (array == null) {
            return new String[0];
        }
        Object[] objArray = (Object[]) array.getArray();
        return Arrays.stream(objArray)
                .map(obj -> obj == null ? null : obj.toString())
                .toArray(String[]::new);
    }

    /**
     * 将 SQL Array 转换为 long 基本类型数组
     */
    private long[] toLongArray(Array array) throws SQLException {
        if (array == null) {
            return new long[0];
        }
        Object[] objArray = (Object[]) array.getArray();
        long[] result = new long[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            if (objArray[i] instanceof Number) {
                result[i] = ((Number) objArray[i]).longValue();
            }
        }
        return result;
    }

    /**
     * 将 SQL Array 转换为 int 基本类型数组
     */
    private int[] toIntArray(Array array) throws SQLException {
        if (array == null) {
            return new int[0];
        }
        Object[] objArray = (Object[]) array.getArray();
        int[] result = new int[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            if (objArray[i] instanceof Number) {
                result[i] = ((Number) objArray[i]).intValue();
            }
        }
        return result;
    }
}
