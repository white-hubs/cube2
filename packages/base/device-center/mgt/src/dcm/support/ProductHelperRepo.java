package com.guhecloud.cube.iot.mgt.dcm.support;

import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.DICT_TYPE_PROD_CAT;


@Slf4j
class ProductHelperRepo {

    private final Map<Long, String> dictTypeCodeById = new HashMap<>();

    private final Map<String, Long> dictTypeIdByCode = new HashMap<>();


    private final Map<DictEntryKey, Long> dictEntryIdByKey = new LinkedHashMap<>();
    private final Map<Long, DictEntryKey> dictEntryKeyById = new LinkedHashMap<>();

    private final Map<Long, SysDictItem> sysDictItemById = new HashMap<>();


    public ProductHelperRepo(Collection<SysDict> sysDicts, Collection<SysDictItem> sysDictItems) {
        for (SysDict sysDict : sysDicts) {
            dictTypeCodeById.put(sysDict.getId(), sysDict.getDictCode());
            dictTypeIdByCode.put(sysDict.getDictCode(), sysDict.getId());
        }

        Map<String, List<SysDictItem>> dictEntriesByDictType = new HashMap<>();
        for (SysDictItem dictItem : sysDictItems) {
            String dictType = getDictTypeCode(dictItem.getDictId());
            sysDictItemById.put(dictItem.getId(), dictItem);
            List<SysDictItem> entries = dictEntriesByDictType.computeIfAbsent(dictType, key -> new ArrayList<>());
            entries.add(dictItem);
        }

        for (Map.Entry<String, List<SysDictItem>> entry : dictEntriesByDictType.entrySet()) {
            List<SysDictItem> dictEntries = entry.getValue();

            // group by parent
            Map<Long, List<SysDictItem>> dictyEntriesByParentId = dictEntries.stream()
                    .collect(Collectors.groupingBy(x -> x.getItemParentId() == null ? 0L : x.getItemParentId()));

            Queue<SysDictItem> inProcessing = new LinkedList<>(dictyEntriesByParentId.get(0L));
            while (!inProcessing.isEmpty()) {
                SysDictItem item = inProcessing.poll();
                DictEntryKey key = buildDictEntryKey(item);
                Long prevId = dictEntryIdByKey.putIfAbsent(key, item.getId());
                if (prevId != null) {
                    log.debug("Duplicate dict entry key: {}, id: {}/{}", key, item.getId(), prevId);
                }
                dictEntryKeyById.put(item.getId(), key);
                inProcessing.addAll(dictyEntriesByParentId.getOrDefault(item.getId(), Collections.emptyList()));
            }
        }
    }

    private DictEntryKey buildDictEntryKey(SysDictItem item) {
        String dictType = dictTypeCodeById.get(item.getDictId());
        SysDictItem parentItem = sysDictItemById.get(item.getItemParentId());
        String parentCode = parentItem == null ? "" : parentItem.getItemLevel();
        String keyCode = getDictEntryKeyCodeBySysDictItem(item);
        return new DictEntryKey(dictType, parentCode,  keyCode);
    }

    private boolean isTreeDict(String dictType) {
        return DICT_TYPE_PROD_CAT.equalsIgnoreCase(dictType);
    }

    public void appendDictEntry(SysDictItem dictItem) {
        Objects.requireNonNull(dictItem.getId(), "SysDictItem with null id");
        Long parentId = dictItem.getItemParentId();
        SysDictItem parent = sysDictItemById.get(dictItem.getItemParentId());
        if (parentId != null && !parentId.equals(0L)) {
            Objects.requireNonNull(parent, "SysDictItem with invalid parentId" + parentId);
        }
        DictEntryKey key = buildDictEntryKey(dictItem);

        dictEntryIdByKey.put(key, dictItem.getId());
        dictEntryKeyById.put(dictItem.getId(), key);
        sysDictItemById.put(dictItem.getId(), dictItem);
    }

    public void appendDictEntries(Collection<SysDictItem> dictItems) {
        for (SysDictItem item: dictItems) {
            appendDictEntry(item);
        }
    }

    DictEntryKey getDictEntryKey(Long dictEntryId) {
        DictEntryKey key = dictEntryKeyById.get(dictEntryId);
        Objects.requireNonNull(key, "SysDictItem id invalid " + dictEntryId);
        return key;
    }

    Long getDictEntryId(DictEntryKey key) {
        Long id = dictEntryIdByKey.get(key);
        Objects.requireNonNull(id, "SysDictItem key invalid " + key);
        return id;
    }

    Long getDictEntryIdOrDefault(DictEntryKey key,  Long defaultVal) {
        Long id = dictEntryIdByKey.get(key);
        if (id == null) {
            log.debug("SysDictItem key invalid {}, default value returned. {} ", key, defaultVal);
        }
        return id == null ? defaultVal : id;
    }
    public String getDictTypeCode(Long dictTypeId) {
        String dictTypeCode = dictTypeCodeById.get(dictTypeId);
        Objects.requireNonNull(dictTypeCode, "dict type id invalid " + dictTypeId);
        return dictTypeCode;
    }

    public Long getDictTypeId(String dictTypeCode) {
        Long dictTypeId = dictTypeIdByCode.get(dictTypeCode);
        Objects.requireNonNull(dictTypeId, "dict type code invalid " + dictTypeCode);
        return dictTypeId;
    }

    public String getDictEntryKeyCodeBySysDictItem(SysDictItem bizEntity) {
        String typeCode = dictTypeCodeById.get(bizEntity.getDictId());
        return isTreeDict(typeCode) ? bizEntity.getItemLevel() : bizEntity.getItemValue();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class DictEntryKey {
        String typeCode;
        String parentPath;
        String entryCode;
    }
}
