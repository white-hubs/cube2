package com.guhecloud.cube.base.dictionary.dcm.controller;

import com.google.common.collect.ImmutableMap;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.service.DictionaryDcmService;
import com.guhecloud.cube.base.dictionary.dcm.utils.DictionaryDcmSupport;
import com.guhecloud.platform.dcm.controller.AbstractBaseDcmController;
import com.guhecloud.platform.dcm.entity.DcmRequirement;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.CONFIG_TYPE_DICT_ENTRY;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam.CONFIG_TYPE_DICT_TYPE;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam.DictTypeParam;
import static com.guhecloud.cube.base.dictionary.dcm.utils.DictionaryDcmSupport.PROVIDER_DICT;
import static com.guhecloud.cube.base.dictionary.dcm.utils.DictionaryDcmSupport.convertDictEntryCollectionParam2RequestedConfigItem;
import static com.guhecloud.cube.base.dictionary.dcm.utils.DictionaryDcmSupport.convertDictTypeParams2DcmRequestedConfigItems;

@Slf4j
@RestController
@RequestMapping("/dict/dcm/config")
public class DictionaryDcmController extends AbstractBaseDcmController {
    private final DictionaryDcmService dictionaryDcmService;
    private static final Map<String, List<String>> SUPPORT_PROVIDER_AND_TYPES = ImmutableMap.of(PROVIDER_DICT,
                                                                                                Arrays.asList(CONFIG_TYPE_DICT_TYPE, CONFIG_TYPE_DICT_ENTRY));

    public DictionaryDcmController(DictionaryDcmService dictionaryDcmService) {
        this.dictionaryDcmService = dictionaryDcmService;
    }

    protected Map<String, List<String>> getSupportedProviderAndTypes() {
        return SUPPORT_PROVIDER_AND_TYPES;
    }

    @Override
    protected Map<String, List<RequestedConfigItem>> fetchRuntimeConfig(Boolean isSystem, String tenant) {
        List<RequestedConfigItem> requestedConfigItems = new ArrayList<>();
        if (isSystem) {
            List<DictTypeParam> dictTypeParams = dictionaryDcmService.fetchRuntimeDictTypes(null);
            if (!dictTypeParams.isEmpty()) {
                requestedConfigItems.add(convertDictTypeParams2DcmRequestedConfigItems(dictTypeParams, false));
            }
        }

        List<DictEntryCollectionParam> dictEntryCollectionParams =
                dictionaryDcmService.fetchRuntimeDictEntryCollections(isSystem ? null : tenant);
        List<String> tenants = isSystem ? null : Collections.singletonList(tenant);

        for (DictEntryCollectionParam dictEntryCollectionParam : dictEntryCollectionParams) {
            RequestedConfigItem converted = convertDictEntryCollectionParam2RequestedConfigItem(
                    dictEntryCollectionParam, false, tenants);
            if (converted != null) {
                requestedConfigItems.add(converted);
            }
        }
        Map<String, List<RequestedConfigItem>> retMap = new HashMap<>();
        retMap.put(PROVIDER_DICT, requestedConfigItems);
        return retMap;
    }

    protected String getBuiltinDcmResourcePath() {
        return "classpath*:dcm/dcm-declaration-dictionary.yml";
    }

    @Override
    protected List<RequestedConfigItem> applyRequestConfigDiffs(Boolean isSystem, String tenant,
                                                                Map<String, List<RequestedConfigItem>> diffs
                                                               ) {
        List<RequestedConfigItem> appliedConfigItems = new ArrayList<>();
        if (isSystem) {
            // 字典类型和系统字典
            handleDictTypes(diffs.getOrDefault(CONFIG_TYPE_DICT_TYPE.toUpperCase(), Collections.emptyList()));
            appliedConfigItems.addAll(diffs.getOrDefault(CONFIG_TYPE_DICT_TYPE.toUpperCase(), Collections.emptyList()));
            handleDictEntries(Collections.emptyList(), diffs.getOrDefault(CONFIG_TYPE_DICT_ENTRY.toUpperCase(),
                                                                          Collections.emptyList()));
            appliedConfigItems.addAll(diffs.getOrDefault(CONFIG_TYPE_DICT_ENTRY.toUpperCase(), Collections.emptyList()));
        } else {
            List<String> tenants;
            tenants = tenant == null ? null : Collections.singletonList(tenant);
            if (!tenants.isEmpty()) {
                handleDictEntries(tenants, diffs.getOrDefault(CONFIG_TYPE_DICT_ENTRY.toUpperCase(), Collections.emptyList()));
                appliedConfigItems.addAll(diffs.getOrDefault(CONFIG_TYPE_DICT_ENTRY.toUpperCase(), Collections.emptyList()));
            }
        }
        return appliedConfigItems;
    }

    private static int getFirstDictEntryPos(List<RequestedConfigItem> diffItems) {
        // 当前这一实现建立在传入的参数中DictType类型的项目始终在DictEntry类型项目这前的假定上。
        return Long.valueOf(diffItems.stream()
                                    .filter(x -> CONFIG_TYPE_DICT_TYPE.equalsIgnoreCase(x.getConfigType()))
                                    .count()).intValue();
    }

    private void handleDictEntries(List<String> tenants, List<RequestedConfigItem> diffs) {
        if (diffs.isEmpty()) {
            return;
        }
        handleDictEntries(tenants, diffs, false);
        handleDictEntries(tenants, diffs, true);
    }

    private void handleDictEntries(List<String> tenants, List<RequestedConfigItem> diffs, boolean toRemove) {
        List<DictEntryCollectionParam> toBeProcessed = diffs.stream()
                .filter(x -> toRemove == Boolean.TRUE.equals(x.getIsRemoval()))
                .map(x -> DictEntryCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(x)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (toBeProcessed.isEmpty()) {
            return;
        }
        dictionaryDcmService.ensureDictEntryExistent(tenants, toBeProcessed, !toRemove);
    }

    private void handleDictTypes(List<RequestedConfigItem> diffs) {
        if (diffs.isEmpty()) {
            return;
        }
        handleDictTypes(diffs, false);
        handleDictTypes(diffs, true);
    }

    private void handleDictTypes(List<RequestedConfigItem> diffs, boolean toRemove) {
        List<DictTypeParam> items = diffs.stream()
                .filter(x -> toRemove == Boolean.TRUE.equals(x.getIsRemoval()))
                .flatMap(x -> DictTypeParam.parseCollectionFrom(RequestedConfigItemParam.fromEntity(x)).stream())
                .collect(Collectors.toList());
        if (items.isEmpty()) {
            return;
        }
        dictionaryDcmService.ensureDictTypeExistent(Collections.emptyList(), items, !toRemove);
    }

    @Override
    protected List<RequestedConfigItem> compare(List<DcmRequirement> requirements, Map<String, List<RequestedConfigItem>> existedConfigs,
                                                boolean isSystem, String tenant, Collection<String> typeFilter
                                               ) {
        return DictionaryDcmSupport.compare(requirements, existedConfigs, isSystem, tenant);
    }
}
