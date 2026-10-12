package com.guhecloud.cube.base.dictionary.dcm.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.platform.dcm.utils.DcmTools.Escape.escape;

/**
 * 映射到SystemDict/DataDict/CascadeDict 的项目的描述.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DictEntryCollectionParam implements Identifiable, Cloneable {
    public static final String CONFIG_TYPE_DICT_ENTRY = "DictEntry";
    public static final String KWKEY_DICT_TYPE = "dictType";
    public static final String KWKEY_PARENT_CODE = "parent";
    public static final String KWKEY_IS_CASCADE = "isCascade";
    public static final String KWKEY_IS_SYSTEM = "isSystem";
    private String dictType;   // 映射到 item_code
    private Boolean isCascade;  // 该项目是否为级联
    private Boolean isSystem;   // 该项目是否系统项
    private String parent;  // 级联code path, 以 "::" 分割
    private Collection<DictEntryParam> dictEntries;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static DictEntryCollectionParam parseFrom(RequestedConfigItemParam param) {
        if (!CONFIG_TYPE_DICT_ENTRY.equalsIgnoreCase(param.getConfigType())) {
            return null;
        }
        // 对于DictEntryCollectionParam, 返回一个空的entry没有意义（即使Remove也是如此）
        if (param.getVargs() == null || CollectionUtils.isEmpty(param.getVargs())) {
            return null;
        }

        Map<String, String> kwargs = param.getKwargs();
        DictEntryCollectionParam dictEntryCollectionParam = new DictEntryCollectionParam();
        dictEntryCollectionParam.setDictType(kwargs.get(KWKEY_DICT_TYPE));
        dictEntryCollectionParam.setParent(kwargs.getOrDefault(KWKEY_PARENT_CODE, ""));

        Object val = kwargs.getOrDefault(KWKEY_IS_CASCADE, "false");
        dictEntryCollectionParam.setIsCascade(Boolean.valueOf(val.toString()));
        dictEntryCollectionParam.setIsSystem(param.isSystem());

        Collection<DictEntryParam> dictEntryParams = new ArrayList<>(param.getVargs().size());
        for (Object varg : param.getVargs()) {
            if (varg instanceof DictEntryParam) {
                dictEntryParams.add((DictEntryParam) varg);
            } else if (varg instanceof Map) {
                dictEntryParams.add(DictEntryParam.from((Map<String, Object>) varg));
            } else if (varg instanceof String) {
                String code = (String) varg;
                dictEntryParams.add(
                        DictEntryParam.builder()
                                .key(code).label(code)
                                .build());
            }
        }
        Map<String, DictEntryParam> deduplicateMap = new HashMap<>();

        for (DictEntryParam dictEntryParam : dictEntryParams) {
            DictEntryParam prev = deduplicateMap.putIfAbsent(dictEntryParam.getId(), dictEntryParam);
            if (prev != null) {
                log.warn("items with duplicated id [{}] found under {} of type: {}, [item1 {} item2:{}]",
                         prev.getId(), dictEntryCollectionParam.getParent(), dictEntryCollectionParam.getDictType(),
                         prev, dictEntryParam);
            }
        }
        dictEntryCollectionParam.setDictEntries(deduplicateMap.values());
        return dictEntryCollectionParam;
    }

    @JsonIgnore
    @Override
    public String getId() {
        // codePath is case-sensitive.
        return String.format("%s:%s|%s|%s|", CONFIG_TYPE_DICT_ENTRY, getDictType(), getIsCascade(), getIsSystem()).toUpperCase() +
               getParent();
    }

    @JsonIgnore
    @Override
    public String getFingerPrint() {
        return parent +
               '[' +
               dictEntries.stream().map(x -> escape(x.getFingerPrint())).collect(Collectors.joining(",")) +
               ']';
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Builder
    public static class DictEntryParam implements Identifiable {
        private String key;  // Key, 映射到code

        private String label;  // Localised Text Label

        @Builder.Default
        private String value = ""; // Value

        @Builder.Default
        private String permState = "";

        @Builder.Default
        private String variable = "";

        @Builder.Default
        private String description = "";

        public static DictEntryParam from(Map<String, Object> map) {
            DictEntryParam dictEntryParam = ConvertUtils.convert(DictEntryParam.class, map);
            if (dictEntryParam.getDescription() == null) {
                dictEntryParam.setDescription("");
            }
            if (dictEntryParam.getValue() == null) {
                dictEntryParam.setValue("");
            }
            if (dictEntryParam.getLabel() == null) {
                dictEntryParam.setLabel(dictEntryParam.getKey());
            }
            return dictEntryParam;
        }

        @JsonIgnore
        @Override
        public String getId() {
            return getKey();
        }

        @JsonIgnore
        @Override
        public String getFingerPrint() {
            return String.format("%s:%s:%s:%s", getKey(), getLabel(),
                                 getValue() == null ? "" : getValue(), getVariable() == null ? "" : getVariable());
        }
    }
}
