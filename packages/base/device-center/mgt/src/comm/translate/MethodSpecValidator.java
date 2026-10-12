package com.guhecloud.cube.iot.mgt.comm.translate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.iot.mgt.comm.util.ParameterCheck;
import com.guhecloud.cube.iot.mgt.core.data.lang.IotDataType;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 产品方法参数定义校验工具
 */
@Slf4j
public class MethodSpecValidator {

    private static final String PARAMS_KEY = "params";
    private static final String CONSTRAINT_TYPE_RANGE = "range";
    private static final String CONSTRAINT_TYPE_ENUM = "enum";
    private static final String CONSTRAINT_TYPE_STRLEN = "strLen";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 校验并规范化方法参数
     */
    public static MethodParamSpec validateAndNormalize(Map<String, Object> inputParams, Map<String, Object> outputParams) {
        Map<String, Object> safeInput = normalizeParams(inputParams, "inputParams");
        Map<String, Object> safeOutput = normalizeParams(outputParams, "outputParams");
        return new MethodParamSpec(safeInput, safeOutput);
    }

    /**
     * 规范化参数定义
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> normalizeParams(Map<String, Object> params, String fieldName) {
        if (params == null || params.isEmpty()) {
            return emptyParams();
        }

        ParameterCheck.checkIsTrue(params.containsKey(PARAMS_KEY), fieldName + "必须包含params数组");
        Object paramsObj = params.get(PARAMS_KEY);
        ParameterCheck.checkIsNotNull(paramsObj, fieldName + "参数格式错误");

        List<Map<String, Object>> paramList;
        if (paramsObj instanceof List) {
            paramList = (List<Map<String, Object>>) paramsObj;
        } else {
            throw new ServiceException(fieldName + "参数格式错误,params必须是数组");
        }

        for (int i = 0; i < paramList.size(); i++) {
            Map<String, Object> param = paramList.get(i);
            ParameterCheck.checkIsNotNull(param, fieldName + "参数格式错误");
            ParameterCheck.checkIsTrue(StringUtils.isNotBlank((String) param.get("name")), "方法参数[" + i + "]名称不能为空");
            ParameterCheck.checkIsTrue(StringUtils.isNotBlank((String) param.get("param_code")), "方法参数[" + i + "]标识不能为空");
            ParameterCheck.checkIsTrue(StringUtils.isNotBlank((String) param.get("data_type")), "方法参数[" + i + "]数据类型不能为空");
        }

        return params;
    }

    /**
     * 解析参数JSON字符串
     */
    public static Map<String, Object> parseParams(String paramsJson, String fieldName) {
        if (StringUtils.isBlank(paramsJson)) {
            return emptyParams();
        }

        try {
            Map<String, Object> parsed = OBJECT_MAPPER.readValue(paramsJson, new TypeReference<Map<String, Object>>() {
            });
            return normalizeParams(parsed, fieldName);
        } catch (JsonProcessingException e) {
            throw new ServiceException(fieldName + "格式错误: " + e.getMessage());
        }
    }

    /**
     * 创建空参数定义
     */
    public static Map<String, Object> emptyParams() {
        return Map.of(PARAMS_KEY, Collections.emptyList());
    }

    /**
     * 将参数对象序列化为JSON字符串
     */
    public static String toJSONString(Map<String, Object> params, String fieldName) {
        Map<String, Object> normalized = normalizeParams(params, fieldName);
        try {
            return OBJECT_MAPPER.writeValueAsString(normalized);
        } catch (JsonProcessingException e) {
            throw new ServiceException(fieldName + "序列化失败: " + e.getMessage());
        }
    }

    /**
     * 校验输入参数
     * 根据数据库中配置的参数规格定义校验实际输入参数
     *
     * @param inputParamsSpec   输入参数规格定义（从数据库查询）
     * @param actualInputParams 实际输入参数值
     * @throws ServiceException 校验失败时抛出异常
     */
    public static void validateInputParams(Map<String, Object> inputParamsSpec, Map<String, Object> actualInputParams) {
        if (inputParamsSpec == null || inputParamsSpec.isEmpty()) {
            log.debug("输入参数规格定义为空，跳过校验");
            return;
        }
        validateParamsBySpec(inputParamsSpec, actualInputParams, "输入参数");
    }

    /**
     * 核心校验逻辑（基于 Map 格式的规格定义）
     *
     * @param paramsSpec   参数规格定义（Map格式，包含params数组）
     * @param actualParams 实际参数值
     * @param paramType    参数类型描述（用于错误提示）
     */
    @SuppressWarnings("unchecked")
    private static void validateParamsBySpec(Map<String, Object> paramsSpec, Map<String, Object> actualParams, String paramType) {
        if (paramsSpec == null || !paramsSpec.containsKey(PARAMS_KEY)) {
            return;
        }

        Object paramsObj = paramsSpec.get(PARAMS_KEY);
        if (!(paramsObj instanceof List)) {
            log.warn("参数规格定义格式错误，params 不是数组类型");
            return;
        }

        List<Map<String, Object>> paramDefList = (List<Map<String, Object>>) paramsObj;
        Map<String, Object> safeParams = actualParams != null ? actualParams : Collections.emptyMap();

        for (Map<String, Object> paramDef : paramDefList) {
            String paramCode = (String) paramDef.get("param_code");
            String paramName = (String) paramDef.get("name");
            String dataType = (String) paramDef.get("data_type");
            Boolean required = (Boolean) paramDef.get("required");
            Object dataConfig = paramDef.get("data_config");

            Object value = safeParams.get(paramCode);

            // 1. 必填校验
            if (Boolean.TRUE.equals(required) && value == null) {
                throw new ServiceException("%s[%s]为必填项".formatted(paramType, paramName));
            }

            if (value == null) {
                continue; // 非必填且为空，跳过后续校验
            }

            // 2. 数据类型校验
            validateDataTypeBySpec(dataType, paramName, value, paramType);

            // 3. 数据范围/格式校验
            if (dataConfig instanceof Map) {
                validateDataConfigBySpec((Map<String, Object>) dataConfig, paramName, value, paramType);
            }
        }
    }

    /**
     * 数据类型校验（基于 Map 格式的规格定义）
     *
     * @param dataType  数据类型
     * @param paramName 参数名称
     * @param value     参数值
     * @param paramType 参数类型描述
     */
    private static void validateDataTypeBySpec(String dataType, String paramName, Object value, String paramType) {
        if (StringUtils.isBlank(dataType)) {
            return;
        }

        IotDataType dataTypeEnum;
        try {
            dataTypeEnum = IotDataType.fromCode(normalizeDataTypeCode(dataType));
        } catch (IllegalArgumentException e) {
            throw new ServiceException("%s[%s]类型错误，期望支持的数据类型".formatted(paramType, paramName));
        }

        switch (dataTypeEnum) {
            case INT:
                if (!(value instanceof Integer) && !isInteger(value)) {
                    throw new ServiceException("%s[%s]类型错误，期望int类型".formatted(paramType, paramName));
                }
                break;
            case LONG:
                if (!(value instanceof Long) && !isLong(value)) {
                    throw new ServiceException("%s[%s]类型错误，期望long类型".formatted(paramType, paramName));
                }
                break;
            case FLOAT:
            case DOUBLE:
                if (!(value instanceof Number)) {
                    throw new ServiceException("%s[%s]类型错误，期望数值类型".formatted(paramType, paramName));
                }
                break;
            case STRING:
                if (!(value instanceof String)) {
                    throw new ServiceException("%s[%s]类型错误，期望string类型".formatted(paramType, paramName));
                }
                break;
            case BOOLEAN:
                if(value instanceof Integer intValue && !(intValue == 1 || intValue == 0)) {
                    throw new ServiceException("%s[%s]类型错误，期望boolean类型".formatted(paramType, paramName));
                }

                break;
            default:
                // 其他类型暂不校验
                log.debug("参数 {} 的数据类型 {} 暂不支持校验", paramName, dataType);
        }
    }

    /**
     * 数据配置校验（基于 Map 格式的规格定义）
     *
     * @param dataConfig 数据配置（Map格式）
     * @param paramName  参数名称
     * @param value      参数值
     * @param paramType  参数类型描述
     */
    private static void validateDataConfigBySpec(Map<String, Object> dataConfig, String paramName, Object value, String paramType) {
        if (dataConfig == null || dataConfig.isEmpty()) {
            return;
        }

        String configType = (String) dataConfig.get("type");
        if (StringUtils.isBlank(configType)) {
            return;
        }

        switch (configType) {
            case CONSTRAINT_TYPE_RANGE:
                validateRangeBySpec(dataConfig, value, paramType, paramName);
                break;
            case CONSTRAINT_TYPE_ENUM:
                validateEnumBySpec(dataConfig, value, paramType, paramName);
                break;
            case CONSTRAINT_TYPE_STRLEN:
                validateStringLengthBySpec(dataConfig, value, paramType, paramName);
                break;
            default:
                log.debug("参数 {} 的配置类型 {} 暂不支持校验", paramName, configType);
                break;
        }
    }

    private static void validateStringLengthBySpec(Map<String, Object> dataConfig, Object value, String paramType, String paramName) {
        if (!(value instanceof String str) ) {
            return;
        }

        Object strLen = dataConfig.get("strLen");

        int len  = Integer.parseInt(String.valueOf(strLen));

        ParameterCheck.checkIsTrue(str.length() <= len, "%s[%s]长度不能超过%s".formatted(paramType, paramName, len));
    }

    /**
     * 范围校验（基于 Map 格式的配置）
     */
    private static void validateRangeBySpec(Map<String, Object> config, Object value, String paramType, String paramName) {
        if (!(value instanceof Number)) {
            return;
        }

        double numValue = ((Number) value).doubleValue();

        Object minObj = config.get("min");
        if (minObj instanceof Number) {
            double min = ((Number) minObj).doubleValue();
            if (numValue < min) {
                throw new ServiceException("%s[%s]不能小于%s".formatted(paramType, paramName, min));
            }
        }

        Object maxObj = config.get("max");
        if (maxObj instanceof Number) {
            double max = ((Number) maxObj).doubleValue();
            if (numValue > max) {
                throw new ServiceException("%s[%s]不能大于%s".formatted(paramType, paramName, max));
            }
        }
    }

    /**
     * 枚举校验（基于 Map 格式的配置）
     * 枚举格式: [{"value":"0","label":"报警"},{"value":"1","label":"非报警"}]
     */
    @SuppressWarnings("unchecked")
    private static void validateEnumBySpec(Map<String, Object> config, Object value, String paramType, String paramName) {
        Object enumValuesObj = config.get("enumValues");
        if (enumValuesObj == null) {
            return;
        }

        List<String> allowedValues;

        // enumValues 是 List<Map> 类型
        if (enumValuesObj instanceof List<?> enumList) {
            if (enumList.isEmpty()) {
                return;
            }

            // 从对象数组中提取 value 字段
            allowedValues = enumList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> {
                        Map<String, Object> map = (Map<String, Object>) item;
                        Object val = map.get("value");
                        return val != null ? String.valueOf(val) : null;
                    })
                    .filter(StringUtils::isNotBlank)
                    .toList();
        } else {
            log.warn("参数 {} 的枚举值类型不支持: {}", paramName, enumValuesObj.getClass());
            throw new ServiceException(" 参数 {} 的枚举值类型不支持", paramName);
        }

        if (allowedValues.isEmpty()) {
            return;
        }

        String strValue = String.valueOf(value);
        if (!allowedValues.contains(strValue)) {
            throw new ServiceException("%s[%s]值无效，允许值: %s".formatted(
                    paramType, paramName, String.join(",", allowedValues)));
        }
    }

    /**
     * 正则校验（基于 Map 格式的配置）
     */
    private static void validatePatternBySpec(Map<String, Object> config, Object value, String paramType, String paramName) {
        Object patternObj = config.get("pattern");
        if (!(patternObj instanceof String pattern)) {
            return;
        }

        String strValue = String.valueOf(value);
        if (!strValue.matches(pattern)) {
            throw new ServiceException("%s[%s]格式不正确".formatted(paramType, paramName));
        }
    }

    /**
     * 兼容别名后归一化数据类型编码
     */
    private static String normalizeDataTypeCode(String dataType) {
        String lowerType = dataType.toLowerCase();
        if ("integer".equals(lowerType)) {
            return IotDataType.INT.code;
        }
        if ("bool".equals(lowerType)) {
            return IotDataType.BOOLEAN.code;
        }
        return lowerType;
    }

    /**
     * 判断值是否可以转换为 Integer
     */
    private static boolean isInteger(Object value) {
        if (value instanceof String) {
            try {
                Integer.parseInt((String) value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断值是否可以转换为 Long
     */
    private static boolean isLong(Object value) {
        if (value instanceof String) {
            try {
                Long.parseLong((String) value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 方法参数规格
     */
    public record MethodParamSpec(Map<String, Object> inputParams, Map<String, Object> outputParams) {
    }
}
