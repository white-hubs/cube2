package com.guhecloud.cube.base.dictionary.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/28 10:13
 * @Description: 业务配置获取工具类型
 */
public class BusinessConfigUtils {

    /**
     * 能力配置模型
     */
    @Data
    public static class Config {
        private String name;
        private Boolean value;
    }

    /**
     * 业务字段配置模型
     */
    @Data
    public static class FiledConfig {
        private String key;
        private String alias;
        private Boolean show;
    }

    /**
     * 能力配置字典编码
     */
    public static final String APP_BUSINESS_CONFIG = "APP_BUSINESS_CONFIG";

    /**
     * 字段别名配置
     */
    public static final String FIELD_CONFIG = "FIELD_CONFIG";

    /**
     * 字段配置
     */
    public static final String FIELDS = "fields";

    /**
     * 根据应用能力编码获取能力配置
     *
     * @param applicationAbilityCode
     *
     * @return
     */
    public static Map<String, Config> getApplicationAbility(String applicationAbilityCode) {
        Map<String, Config> dataDictionaryVariable = getDataDictionaryVariable(APP_BUSINESS_CONFIG, applicationAbilityCode,
                                                                               new TypeReference<Map<String, Config>>() {
                                                                               });
        return MapUtils.isNotEmpty(dataDictionaryVariable) ? dataDictionaryVariable : new HashMap<>();
    }

    /**
     * 根据业务字段配置编码获取配置详情
     *
     * @param businessFileConfigCode 业务子段配置编码
     *
     * @return
     */
    public static Map<String, FiledConfig> getFieldNameConfig(String businessFileConfigCode) {
        Map<String, List<FiledConfig>> filedConfigMap =
                getDataDictionaryVariable(FIELD_CONFIG, businessFileConfigCode, new TypeReference<Map<String, List<FiledConfig>>>() {});
        if (MapUtils.isEmpty(filedConfigMap) || CollectionUtils.isEmpty(filedConfigMap.get(FIELDS))) {
            return new HashMap<>();
        }
        return filedConfigMap.get(FIELDS).stream().collect(Collectors.toMap(FiledConfig::getKey, Function.identity(), (k1, k2) -> k2));
    }

    public static <T> T getDataDictionaryVariable(String itemCode, String code, TypeReference<T> typeReference) {
        DataDictionaryaramByItemAndCodeParam dictionaryaram = new DataDictionaryaramByItemAndCodeParam();
        dictionaryaram.setItemCode(itemCode);
        dictionaryaram.setCode(code);
        DataDictionaryAdapter dataDictionaryAdapter = ApplicationContextHolder.getBean(DataDictionaryAdapter.class);
        DataDictionaryDTO dataDictionary = dataDictionaryAdapter.getDataDictionaryByItemAndCode(dictionaryaram);
        if (Objects.isNull(dataDictionary) || StringUtils.isBlank(dataDictionary.getVariable())) {
            return null;
        }
        return JsonUtils.deserialize(dataDictionary.getVariable(), typeReference);
    }

    /**
     * 根据code判断是否具有该业务能力
     *
     * @param applicationAbilityCode 应用编码
     * @param abilityCode            能力编码
     *
     * @return
     */
    public static boolean abilityFlag(String applicationAbilityCode, String abilityCode) {
        Map<String, Config> cacheVariable = getApplicationAbility(applicationAbilityCode);
        Config config = cacheVariable.get(abilityCode);
        return config != null && config.getValue() != null ? config.getValue() : false;
    }

}
