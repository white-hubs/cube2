package com.guhecloud.cube.iot.mgt.dcm.support;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.SysDict;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.dcm.entity.DictEntry;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.support.AbstractGroupedDcmHelper;
import com.guhecloud.platform.dcm.support.IDcmRciConverter;
import com.guhecloud.platform.dcm.support.ITreeDcmHelper;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.CONFIG_TYPE_DICT_ENTRY;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.DICT_TYPE_PROD_CAT;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.KW_KEY_PARENT;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.KW_KEY_TYPE_CODE;
import static com.guhecloud.cube.iot.mgt.dcm.support.IotDcmSupport.parseParent;

public class DictEntryDcmHelper extends AbstractGroupedDcmHelper<SysDictItem, DictEntry>
        implements ITreeDcmHelper<SysDictItem, DictEntry> {

    private final ProductHelperRepo helperRepo;
    private final PlatformEvnCom platformEvnCom;

    public DictEntryDcmHelper(Collection<SysDict> sysDicts, Collection<SysDictItem> dictItems) {
        this(sysDicts, dictItems, null);
    }

    public DictEntryDcmHelper(Collection<SysDict> sysDicts, Collection<SysDictItem> dictItems, PlatformEvnCom platformEvnCom) {
        super(SysDictItem.class, DictEntry.class);
        helperRepo = new ProductHelperRepo(sysDicts, dictItems);
        this.platformEvnCom = platformEvnCom;
    }

    public static IDcmRciConverter<DictEntry> createRciComparator() {
        return new DictEntryDcmHelper(Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public boolean isTreeStyle() {
        return true;
    }

    @Override
    protected Object generateGroupKey(DictEntry dcmEntity) {
        return new DictEntryGroupKey(dcmEntity.getTypeCode(), dcmEntity.getParentCode());
    }

    @Override
    protected RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DictEntry> dcmEntities) {
        RequestedConfigItem rci = getRciTemplate();
        DictEntryGroupKey key = (DictEntryGroupKey) groupKey;
        rci.setKwargs(KW_KEY_TYPE_CODE, key.getDictType());
        if (StringUtil.isNotBlank(key.getParentCode())) {
            rci.setKwargs(KW_KEY_PARENT, key.getParentCode());
        }
        rci.setVargs(dcmEntities.stream()
                             .filter(x -> x.getKey() != null)   // if a DictEntry's key is null, we would ignore it
                             .sorted((id1, id2) -> keyCompare(id1.getKey(), id2.getKey()))
                             .collect(Collectors.toList()));
        if (log.isDebugEnabled()) {
            if (dcmEntities.size() != rci.getVargs().size()) {
                log.debug("build rci for group key {} have filtered {} items with empty key.", groupKey,
                          dcmEntities.size() - rci.getVargs().size());
            }
        }
        return rci;
    }

    @Override
    protected void updateDcmGroupInfoWithRci(Collection<? extends DictEntry> dcmEntities, RequestedConfigItem rci) {
        String typeCode = rci.getKwargs(KW_KEY_TYPE_CODE, null);
        String parent = rci.getKwargs(KW_KEY_PARENT, "");
        Objects.requireNonNull(typeCode, "type code invalid in RCI");
        for (DictEntry entry : dcmEntities) {
            entry.setParentCode(parent);
            entry.setTypeCode(typeCode);
        }
    }

    @Override
    protected void updateBizGroupInfoWithDcm(SysDictItem bizEntity, DictEntry dcmEntity) {
        Long dictTypeId = helperRepo.getDictTypeId(dcmEntity.getTypeCode());
        bizEntity.setDictId(dictTypeId);
        bizEntity.setItemParentId(StringUtil.isBlank(dcmEntity.getParentCode()) ? 0L :
                                  getParentId(dcmEntity.getTypeCode(), dcmEntity.getParentCode()));
        // 更新未对齐的字段
        bizEntity.setItemLevel(isTreeDictEntry(dcmEntity.getTypeCode()) ? dcmEntity.getKey() : "");
        bizEntity.setItemValue(dcmEntity.getKey());
        bizEntity.setItemLabel(dcmEntity.getLabel());
    }

    private Long getParentId(String typeCode, String parentCode) {
        if (StringUtil.isBlank(parentCode)) {
            return 0L;
        }
        String parentOfParent = parseParent(parentCode);
        return helperRepo.getDictEntryId(new ProductHelperRepo.DictEntryKey(typeCode, parentOfParent, parentCode));
    }

    boolean isTreeDictEntry(String typeCode) {
        return DICT_TYPE_PROD_CAT.equalsIgnoreCase(typeCode);
    }

    private String getDictEntryKeyFromSysDictItem(SysDictItem bizEntity) {
        return helperRepo.getDictEntryKeyCodeBySysDictItem(bizEntity);
    }

    private void updateBizDictEntryKeyInfo(SysDictItem input, DictEntry dcmEntity) {
        input.setItemValue(dcmEntity.getLabel()); // 当前的行为。
        input.setItemLevel(isTreeDictEntry(dcmEntity.getTypeCode()) ? dcmEntity.getKey() : "");
    }

    @Override
    protected void updateDcmGroupInfoWithBiz(DictEntry dcmEntity, SysDictItem bizEntity) {
        dcmEntity.setTypeCode(helperRepo.getDictTypeCode(bizEntity.getDictId()));
        dcmEntity.setParentCode(getParentCode(bizEntity.getItemParentId()));
        dcmEntity.setKey(getDictEntryKeyFromSysDictItem(bizEntity));
        dcmEntity.setLabel(bizEntity.getItemLabel());
        if (dcmEntity.getRemark() == null) {
            dcmEntity.setRemark("");
        }
    }

    private String getParentCode(Long itemParentId) {
        if (itemParentId == null || itemParentId.equals(0L)) {
            return "";
        } else {
            return helperRepo.getDictEntryKey(itemParentId).getEntryCode();
        }
    }

    private String getBizItemDictKey(String typeCode, SysDictItem bizEntity) {
        return DICT_TYPE_PROD_CAT.equals(typeCode) ? bizEntity.getItemLevel() : bizEntity.getItemValue();
    }

    @Override
    public String getBizKey(SysDictItem bizEntity) {
        String typeCode = helperRepo.getDictTypeCode(bizEntity.getDictId());
        return String.format("%s:%s:%s", typeCode,
                             getParentCode(bizEntity.getItemParentId()), getBizItemDictKey(typeCode, bizEntity));
    }

    @Override
    public void updateBizWithDcm(SysDictItem input, DictEntry dcmEntity) {
        input.setRemark(dcmEntity.getRemark());
        input.setItemLabel(dcmEntity.getLabel());
        input.setStatus(dcmEntity.getStatus());
        updateBizDictEntryKeyInfo(input, dcmEntity);
    }

    @Override
    protected String getConfigType() {
        return CONFIG_TYPE_DICT_ENTRY;
    }

    @Override
    protected DictEntry dcmFromString(String object) {
        DictEntry entry = super.dcmFromString(object);
        if (entry == null) {
            entry = DictEntry.builder().key(object).label(object).build();
        }
        return entry;
    }

    @Override
    public DictEntry convertBiz2Dcm(SysDictItem bizEntity) {
        DictEntry dictEntry = super.convertBiz2Dcm(bizEntity);
        if (dictEntry != null) {
            dictEntry.setItemSort(bizEntity.getItemSort() == null ? 0 : bizEntity.getItemSort());
            dictEntry.setRemark(bizEntity.getRemark() == null ? "" : bizEntity.getRemark());
        }
        return dictEntry;
    }

    protected Comparator<DictEntry> getDcmComparator() {
        return this::dictEntryKeyCompare;
    }

    @Override
    public void treeSaveBatch(IService<SysDictItem> service, Collection<DictEntry> entities) {
        Map<DictEntryGroupKey, List<DictEntry>> sortedDictEntries = entities.stream()
                .filter(x -> x.getKey() != null)
                .sorted(this::dictEntryKeyCompare) // 可以完成自然的排序。
                .collect(Collectors.groupingBy(x ->
                                                       (DictEntryGroupKey) generateGroupKey(x), LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<DictEntryGroupKey, List<DictEntry>> entry : sortedDictEntries.entrySet()) {
            try {
                List<SysDictItem> items2Add = entry.getValue().stream()
                        .map(this::convertDcm2Biz)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                if (items2Add.size() < entry.getValue().size()) {
                    log.warn("treeSaveBatch: {} items were ignored in {} items for {}",
                             entry.getValue().size() - items2Add.size(), entry.getValue().size(), entry.getKey());
                }
                for (SysDictItem item : items2Add) {
                    item.setId(platformEvnCom.nextSnowflakeId());
                }
                service.saveBatch(items2Add);
                helperRepo.appendDictEntries(items2Add);
            } catch (RuntimeException re) {
                log.debug("unexpected exception ", re);
                // throw re;
            }
        }
    }

    private int dictEntryKeyCompare(DictEntry entry1, DictEntry entry2) {
        int typeCompareRet = entry1.getTypeCode().compareTo(entry2.getTypeCode());
        if (typeCompareRet != 0) {
            return typeCompareRet;
        }
        int parentCompareRet = keyCompare(entry1.getParentCode(), entry2.getParentCode());
        if (parentCompareRet != 0) {
            return parentCompareRet;
        }
        return entry1.getKey().compareTo(entry2.getKey());
    }

    private int keyCompare(String key1, String key2) {
        int l1 = StringUtil.isBlank(key1) ? 0 : key1.length();
        int l2 = StringUtil.isBlank(key2) ? 0 : key2.length();
        if (l1 != l2) {
            return l1 - l2;
        }
        return key1.compareTo(key2);
    }

    @Data
    @AllArgsConstructor
    static class DictEntryGroupKey {
        String dictType;
        String parentCode;
    }
}
