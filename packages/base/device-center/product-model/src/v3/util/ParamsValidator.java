package com.guhecloud.cube.base.iot.product.model.v3.util;

import com.guhecloud.cube.base.iot.product.model.v3.enums.DataTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.DataConfig;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.EnumValue;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.ParamSpec;
import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 参数规格统一校验器
 *
 * 用于校验方法参数和事件参数,确保参数规格的合法性和一致性
 */
public class ParamsValidator {

    /**
     * 支持的数据类型集合
     * 基于 DataTypeEnum 定义的类型
     */
    private static final Set<String> SUPPORTED_DATA_TYPES = new HashSet<>(
            Arrays.asList("int", "long", "float", "double", "string", "boolean", "bool", "enum", "date", "struct", "object", "array")
    );

    /**
     * 校验参数规格列表
     *
     * @param params 参数对象
     * @param paramType 参数类型描述 (如"输入参数"、"输出参数"、"事件参数")
     * @throws InvalidArgumentException 当参数不合法时抛出
     */
    public static void validate(Params params, String paramType) {
        if (params == null || params.getParams() == null) {
            return;
        }

        List<ParamSpec> paramSpecs = params.getParams();
        Set<String> paramCodes = new HashSet<>();

        for (int i = 0; i < paramSpecs.size(); i++) {
            ParamSpec paramSpec = paramSpecs.get(i);

            // 校验参数对象非空
            if (paramSpec == null) {
                throw new InvalidArgumentException(String.format("%s[%d]不能为空", paramType, i));
            }

            // 校验参数名称
            if (!StringUtils.hasText(paramSpec.getName())) {
                throw new InvalidArgumentException(String.format("%s[%d]的参数名称不能为空", paramType, i));
            }

            // 校验参数编码
            if (!StringUtils.hasText(paramSpec.getParamCode())) {
                throw new InvalidArgumentException(String.format("%s[%d]的参数编码不能为空", paramType, i));
            }

            // 校验参数编码唯一性
            String paramCode = paramSpec.getParamCode();
            if (paramCodes.contains(paramCode)) {
                throw new InvalidArgumentException(String.format("%s编码重复: %s", paramType, paramCode));
            }
            paramCodes.add(paramCode);

            // 校验数据类型非空
            if (!StringUtils.hasText(paramSpec.getDataType())) {
                throw new InvalidArgumentException(String.format("%s[%d]的数据类型不能为空", paramType, i));
            }

            // 校验数据类型合法性
            if (!isValidDataType(paramSpec.getDataType())) {
                throw new InvalidArgumentException(
                        String.format("%s[%d]的数据类型不合法: %s, 支持的类型: %s",
                                paramType, i, paramSpec.getDataType(), SUPPORTED_DATA_TYPES));
            }

            // 校验数据配置
            if (paramSpec.getDataConfig() != null) {
                validateDataConfig(paramSpec.getDataConfig(), paramType, i);
            }
        }
    }

    /**
     * 校验数据类型是否合法
     *
     * @param dataType 数据类型
     * @return true表示合法,false表示不合法
     */
    private static boolean isValidDataType(String dataType) {
        if (!StringUtils.hasText(dataType)) {
            return false;
        }
        // 使用 DataTypeEnum 进行校验
        return DataTypeEnum.fromCode(dataType.toLowerCase()) != null;
    }

    /**
     * 校验数据配置
     *
     * @param config 数据配置
     * @param paramType 参数类型描述
     * @param index 参数索引
     * @throws InvalidArgumentException 当配置不合法时抛出
     */
    private static void validateDataConfig(DataConfig config, String paramType, int index) {
        if (!StringUtils.hasText(config.getType())) {
            throw new InvalidArgumentException(String.format("%s[%d]的数据配置类型不能为空", paramType, index));
        }

        String configType = config.getType();
        switch (configType.toLowerCase()) {
            case "range":
                validateRangeConfig(config, paramType, index);
                break;
            case "enum":
                validateEnumConfig(config, paramType, index);
                break;
            case "pattern":
                validatePatternConfig(config, paramType, index);
                break;
            case "strlen":
                validateStrLenConfig(config, paramType, index);
                break;
            default:
                throw new InvalidArgumentException(
                        String.format("%s[%d]的数据配置类型不支持: %s", paramType, index, configType));
        }
    }

    /**
     * 校验范围配置
     */
    private static void validateRangeConfig(DataConfig config, String paramType, int index) {
        if (config.getMin() == null || config.getMax() == null) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的range配置必须包含min和max", paramType, index));
        }
        // 比较 min 和 max 的大小
        double minValue = config.getMin().doubleValue();
        double maxValue = config.getMax().doubleValue();
        if (minValue > maxValue) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的range配置min不能大于max", paramType, index));
        }
    }

    /**
     * 校验枚举配置
     */
    private static void validateEnumConfig(DataConfig config, String paramType, int index) {
        List<EnumValue> enumValues = config.getEnumValues();
        if (enumValues == null || enumValues.isEmpty()) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的enum配置必须包含枚举值列表", paramType, index));
        }

        Set<String> enumValueSet = new HashSet<>();
        for (EnumValue enumValue : enumValues) {
            if (enumValue == null || !StringUtils.hasText(enumValue.getValue())) {
                throw new InvalidArgumentException(
                        String.format("%s[%d]的枚举值不能为空", paramType, index));
            }
            if (!enumValueSet.add(enumValue.getValue())) {
                throw new InvalidArgumentException(
                        String.format("%s[%d]的枚举值重复: %s", paramType, index, enumValue.getValue()));
            }
        }
    }

    /**
     * 校验正则表达式配置
     */
    private static void validatePatternConfig(DataConfig config, String paramType, int index) {
        if (!StringUtils.hasText(config.getPattern())) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的pattern配置必须包含正则表达式", paramType, index));
        }
        // 可以添加正则表达式合法性校验
        try {
            java.util.regex.Pattern.compile(config.getPattern());
        } catch (Exception e) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的pattern配置不是合法的正则表达式: %s", paramType, index, config.getPattern()));
        }
    }

    /**
     * 校验字符串长度配置
     */
    private static void validateStrLenConfig(DataConfig config, String paramType, int index) {
        if (config.getLen() == null || config.getLen() <= 0) {
            throw new InvalidArgumentException(
                    String.format("%s[%d]的strLen配置必须大于0", paramType, index));
        }
    }
}
