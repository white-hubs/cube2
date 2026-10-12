package com.guhecloud.cube.iot.mgt.things.vo.param;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Params 参数规格校验器
 * 用于校验 Params 及其内部 ParamSpec 的合法性
 *
 * @author system
 * @date 2026-01-17
 */
public class ParamsValidator {

    /**
     * 支持的数据类型
     */
    private static final Set<String> VALID_DATA_TYPES = new HashSet<>(Arrays.asList(
            "int", "float", "double", "string", "boolean", "enum", "date", "datetime"
    ));

    /**
     * 数值类型
     */
    private static final Set<String> NUMERIC_TYPES = new HashSet<>(Arrays.asList(
            "int", "float", "double"
    ));

    /**
     * 字符串类型
     */
    private static final Set<String> STRING_TYPES = new HashSet<>(Arrays.asList(
            "string"
    ));

    /**
     * 校验参数列表
     *
     * @param params 待校验的参数列表
     * @return 校验结果，包含所有错误信息
     */
    public static ValidationResult validate(List<ParamSpec> params) {
        ValidationResult result = new ValidationResult();

        if (params == null || params.isEmpty()) {
            result.addError("参数列表不能为空");
            return result;
        }

        // 校验参数编码唯一性
        Set<String> paramCodes = new HashSet<>();
        for (int i = 0; i < params.size(); i++) {
            ParamSpec paramSpec = params.get(i);
            String prefix = "参数[" + i + "]";

            // 校验单个 ParamSpec
            validateParamSpec(paramSpec, prefix, result);

            // 校验参数编码唯一性
            if (paramSpec != null && StringUtils.isNotBlank(paramSpec.getParamCode())) {
                if (paramCodes.contains(paramSpec.getParamCode())) {
                    result.addError(prefix + " 参数编码重复: " + paramSpec.getParamCode());
                } else {
                    paramCodes.add(paramSpec.getParamCode());
                }
            }
        }

        return result;
    }

    /**
     * 校验单个 ParamSpec
     *
     * @param paramSpec 待校验的 ParamSpec
     * @param prefix    错误信息前缀
     * @param result    校验结果
     */
    private static void validateParamSpec(ParamSpec paramSpec, String prefix, ValidationResult result) {
        if (paramSpec == null) {
            result.addError(prefix + " 不能为空");
            return;
        }

        // 1. 基础字段非空校验
        if (StringUtils.isBlank(paramSpec.getName())) {
            result.addError(prefix + " name 不能为空");
        }

        if (StringUtils.isBlank(paramSpec.getParamCode())) {
            result.addError(prefix + " paramCode 不能为空");
        }

        if (StringUtils.isBlank(paramSpec.getDataType())) {
            result.addError(prefix + " dataType 不能为空");
            return; // dataType 为空则无法继续校验
        }

        // 2. dataType 合法性校验
        String dataType = paramSpec.getDataType().toLowerCase();
        if (!VALID_DATA_TYPES.contains(dataType)) {
            result.addError(prefix + " dataType 不合法: " + paramSpec.getDataType() +
                    ", 支持的类型: " + VALID_DATA_TYPES);
            return; // dataType 不合法则无法继续校验
        }

        // 3. DataConfig 校验
        DataConfig dataConfig = paramSpec.getDataConfig();
        if (dataConfig != null) {
            validateDataConfig(dataConfig, dataType, prefix, result);
        } else {
            // enum 和 boolean 类型必须有 DataConfig
            if ("enum".equals(dataType) || "boolean".equals(dataType)) {
                result.addError(prefix + " " + dataType + " 类型必须配置 dataConfig");
            }
        }

        // 4. 精度校验（仅针对浮点数）
        if (("float".equals(dataType) || "double".equals(dataType)) && paramSpec.getPrecision() != null) {
            if (paramSpec.getPrecision() < 0) {
                result.addError(prefix + " precision 不能为负数");
            }
        }
    }

    /**
     * 校验 DataConfig
     *
     * @param dataConfig DataConfig 对象
     * @param dataType   参数的数据类型
     * @param prefix     错误信息前缀
     * @param result     校验结果
     */
    private static void validateDataConfig(DataConfig dataConfig, String dataType,
                                           String prefix, ValidationResult result) {
        String configType = dataConfig.getType();
        if (StringUtils.isBlank(configType)) {
            result.addError(prefix + " dataConfig.type 不能为空");
            return;
        }

        configType = configType.toLowerCase();

        // 校验 DataConfig 类型与 dataType 的匹配性
        switch (configType) {
            case "range":
                validateRangeConfig(dataConfig, dataType, prefix, result);
                break;
            case "enum":
                validateEnumConfig(dataConfig, dataType, prefix, result);
                break;
            case "pattern":
                validatePatternConfig(dataConfig, dataType, prefix, result);
                break;
            case "strlen":
                validateStrLenConfig(dataConfig, dataType, prefix, result);
                break;
            default:
                result.addError(prefix + " dataConfig.type 不合法: " + configType +
                        ", 支持的类型: range, enum, pattern, strLen");
        }
    }

    /**
     * 校验 range 类型配置
     */
    private static void validateRangeConfig(DataConfig dataConfig, String dataType,
                                           String prefix, ValidationResult result) {
        // range 类型只适用于数值类型
        if (!NUMERIC_TYPES.contains(dataType)) {
            result.addError(prefix + " range 配置只适用于数值类型(int/float/double), 当前类型: " + dataType);
            return;
        }

        Number min = dataConfig.getMin();
        Number max = dataConfig.getMax();

        if (min == null && max == null) {
            result.addError(prefix + " range 配置至少需要指定 min 或 max");
            return;
        }

        // 校验 min <= max
        if (min != null && max != null) {
            if (min.doubleValue() > max.doubleValue()) {
                result.addError(prefix + " range 配置中 min(" + min + ") 不能大于 max(" + max + ")");
            }
        }

        // 校验 step
        Number step = dataConfig.getStep();
        if (step != null && step.doubleValue() <= 0) {
            result.addError(prefix + " range 配置中 step 必须大于 0");
        }
    }

    /**
     * 校验 enum 类型配置
     */
    private static void validateEnumConfig(DataConfig dataConfig, String dataType,
                                          String prefix, ValidationResult result) {
        // enum 配置适用于 enum 和 boolean 类型
        if (!"enum".equals(dataType) && !"boolean".equals(dataType)) {
            result.addError(prefix + " enum 配置只适用于 enum 或 boolean 类型, 当前类型: " + dataType);
            return;
        }

        List<EnumValue> enumValues = dataConfig.getEnumValues();
        if (enumValues == null || enumValues.isEmpty()) {
            result.addError(prefix + " enum 配置的 enumValues 不能为空");
            return;
        }

        // 校验 boolean 类型的枚举值数量
        if ("boolean".equals(dataType) && enumValues.size() != 2) {
            result.addError(prefix + " boolean 类型的 enumValues 必须包含 2 个值");
        }

        // 校验枚举值的唯一性和非空性
        Set<String> values = new HashSet<>();
        for (int i = 0; i < enumValues.size(); i++) {
            EnumValue enumValue = enumValues.get(i);
            if (enumValue == null) {
                result.addError(prefix + " enumValues[" + i + "] 不能为空");
                continue;
            }

            if (StringUtils.isBlank(enumValue.getValue())) {
                result.addError(prefix + " enumValues[" + i + "].value 不能为空");
            } else {
                if (values.contains(enumValue.getValue())) {
                    result.addError(prefix + " enumValues 中存在重复的 value: " + enumValue.getValue());
                } else {
                    values.add(enumValue.getValue());
                }
            }

            if (StringUtils.isBlank(enumValue.getLabel())) {
                result.addError(prefix + " enumValues[" + i + "].label 不能为空");
            }
        }
    }

    /**
     * 校验 pattern 类型配置
     */
    private static void validatePatternConfig(DataConfig dataConfig, String dataType,
                                             String prefix, ValidationResult result) {
        // pattern 配置只适用于字符串类型
        if (!STRING_TYPES.contains(dataType)) {
            result.addError(prefix + " pattern 配置只适用于 string 类型, 当前类型: " + dataType);
            return;
        }

        String pattern = dataConfig.getPattern();
        if (StringUtils.isBlank(pattern)) {
            result.addError(prefix + " pattern 配置的 pattern 不能为空");
            return;
        }

        // 校验正则表达式的合法性
        try {
            java.util.regex.Pattern.compile(pattern);
        } catch (Exception e) {
            result.addError(prefix + " pattern 配置的正则表达式不合法: " + e.getMessage());
        }
    }

    /**
     * 校验 strLen 类型配置
     */
    private static void validateStrLenConfig(DataConfig dataConfig, String dataType,
                                            String prefix, ValidationResult result) {
        // strLen 配置只适用于字符串类型
        if (!STRING_TYPES.contains(dataType)) {
            result.addError(prefix + " strLen 配置只适用于 string 类型, 当前类型: " + dataType);
            return;
        }

        Integer len = dataConfig.getLen();
        if (len == null) {
            result.addError(prefix + " strLen 配置的 len 不能为空");
        } else if (len <= 0) {
            result.addError(prefix + " strLen 配置的 len 必须大于 0");
        }
    }

    /**
     * 校验结果类
     */
    @Getter
    public static class ValidationResult {
        /**
         * -- GETTER --
         *  获取所有错误信息
         */
        private final List<String> errors = new ArrayList<>();

        /**
         * 添加错误信息
         */
        public void addError(String error) {
            errors.add(error);
        }

        /**
         * 是否校验通过
         */
        public boolean isValid() {
            return errors.isEmpty();
        }

        /**
         * 获取格式化的错误信息
         */
        public String getErrorMessage() {
            if (errors.isEmpty()) {
                return "";
            }
            return String.join("; ", errors);
        }

        @Override
        public String toString() {
            if (isValid()) {
                return "校验通过";
            }
            return "校验失败: " + getErrorMessage();
        }
    }
}
