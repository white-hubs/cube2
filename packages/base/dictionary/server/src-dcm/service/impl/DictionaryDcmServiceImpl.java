package com.guhecloud.cube.base.dictionary.dcm.service.impl;

import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.DictEntryParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam.DictTypeParam;
import com.guhecloud.cube.base.dictionary.dcm.service.DictionaryDcmService;
import com.guhecloud.cube.base.dictionary.server.constant.DictionaryConstant;
import com.guhecloud.cube.base.dictionary.server.constant.VersionConstant;
import com.guhecloud.cube.base.dictionary.server.dao.DataDictionaryMapper;
import com.guhecloud.cube.base.dictionary.server.entity.CascadeDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.DataDictionary;
import com.guhecloud.cube.base.dictionary.server.entity.SysDictionary;
import com.guhecloud.cube.base.dictionary.server.enumeration.StatusEnum;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryUpdateParam;
import com.guhecloud.cube.base.dictionary.server.param.DataDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.DictionaryItemParam;
import com.guhecloud.cube.base.dictionary.server.service.CascadeDictionaryService;
import com.guhecloud.cube.base.dictionary.server.service.DataDictionaryServer;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryItemService;
import com.guhecloud.cube.base.dictionary.server.service.SysDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeDictionaryVO;
import com.guhecloud.cube.base.dictionary.server.vo.DataDictionaryVO;
import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;
import com.guhecloud.cube.base.dictionary.server.vo.SysDictionaryVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.platform.dcm.support.DcmSupport.executeWithTenant;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Controller
public class DictionaryDcmServiceImpl implements DictionaryDcmService {
    private static final String EMPTY_PARENT_CODE = "";
    private static final String PARENT_CODE_SEPARATOR = "::";
    private static final String DICT_TYPE_KEY_TEMPLATE = "%s" + PARENT_CODE_SEPARATOR + "%s";
    private final SysDictionaryService sysDictionaryService;
    private final DataDictionaryServer dataDictionaryServer;
    private final DictionaryItemService dictionaryItemService;
    private final CascadeDictionaryService cascadeDictionaryService;
    private final DataDictionaryMapper dataDictionaryMapper;

    public DictionaryDcmServiceImpl(SysDictionaryService sysDictionaryService, DataDictionaryServer dataDictionaryServer,
                                    DictionaryItemService dictionaryItemService, CascadeDictionaryService cascadeDictionaryService,
                                    DataDictionaryMapper dataDictionaryMapper
                                   ) {
        this.sysDictionaryService = sysDictionaryService;
        this.dataDictionaryServer = dataDictionaryServer;
        this.dictionaryItemService = dictionaryItemService;
        this.cascadeDictionaryService = cascadeDictionaryService;
        this.dataDictionaryMapper = dataDictionaryMapper;
    }

    @Override
    public void ensureDictTypeExistent(List<String> tenants, List<DictTypeParam> dictTypes, boolean isExists) {
        if (dictTypes.isEmpty()) {
            return;
        }
        if (tenants != null && !tenants.isEmpty()) {
            log.warn("DictTypes SHOULD NOT be updated with tenants: {}", tenants);
        }
        log.debug("ensureDictTypeExistent dictTypes: {}, isExists: {}", dictTypes, isExists);
        if (isExists) {
            boolean ret = dictionaryItemService.batchAddOrUpdate(dictTypes.stream()
                                                                         .map(this::convertDictTypeParam2DictionaryItemParam)
                                                                         .collect(Collectors.toList()));
            if (!ret) {
                log.warn("batchAddOrUpdate dictTypes failed, see logs for more info");
            }
        } else {
            int count = dictionaryItemService.batchRemoveByCode(dictTypes.stream()
                                                                        .map(DictTypeParam::getCode)
                                                                        .collect(Collectors.toList()));
            log.info("batchRemove {} dictTypes, returned: {}", dictTypes.size(), count);
        }
    }

    private DictionaryItemParam convertDictTypeParam2DictionaryItemParam(DictTypeParam dictTypeParam) {
        DictionaryItemParam ret = new DictionaryItemParam();
        ret.setCode(dictTypeParam.getCode());
        ret.setRemarks(dictTypeParam.getRemark());
        return ret;
    }

    @Override
    public void ensureDictEntryExistent(
            List<String> tenants, List<DictEntryCollectionParam> dictEntryCollectionParams,
            boolean isExists
                                       ) {
        if (dictEntryCollectionParams.isEmpty()) {
            log.warn("ensureDictEntryExistent called with empty entry collection, skipped");
            return;
        }
        // param checking.
        boolean isSystem = (tenants == null || tenants.isEmpty());
        for (DictEntryCollectionParam dictEntryCollectionParam : dictEntryCollectionParams) {
            if (Boolean.TRUE.equals(dictEntryCollectionParam.getIsSystem()) != isSystem) {
                log.error("ensureDictEntryExists: entry id: {},  tenants: {}, " +
                          "isSystem feature of entry mismatch with tenant requests. ",
                          dictEntryCollectionParam.getId(), tenants);
                throw new RuntimeException("isSystem Rule breached.");
            }
        }

        if (isSystem) {
            ensureSysDictEntryExistent(dictEntryCollectionParams, isExists);
        } else {
            ensureTenantDictEntryExistent(tenants, dictEntryCollectionParams, isExists);
        }
    }

    private void ensureTenantDictEntryExistent(List<String> tenants, List<DictEntryCollectionParam> dictEntryCollectionParams,
                                               boolean isExists
                                              ) {
        if (tenants == null || tenants.isEmpty()) {
            log.info("ensureTenantDictEntryExistent: tenants is empty, skip");
            return;
        }
        List<Long> tenantIds = tenants.stream().map(Long::parseLong).collect(Collectors.toList());

        List<DictEntryCollectionParam> dataDictEntries = new ArrayList<>();
        List<DictEntryCollectionParam> cascadeDictEntries = new ArrayList<>();
        for (DictEntryCollectionParam dictEntryCollectionParam : dictEntryCollectionParams) {
            if (Boolean.TRUE.equals(dictEntryCollectionParam.getIsCascade())) {
                cascadeDictEntries.add(dictEntryCollectionParam);
            } else {
                dataDictEntries.add(dictEntryCollectionParam);
            }
        }
        if (!cascadeDictEntries.isEmpty()) {
            // XXX： 鉴于实现的复杂性，先用逐租户的方案做。
            ensureCascadeDictEntryExists(tenantIds, cascadeDictEntries, isExists);
        }
        if (!dataDictEntries.isEmpty()) {
            ensureDataDictEntryExistent(tenantIds, dataDictEntries, isExists);
        }
    }

    private void ensureDataDictEntryExistent(List<Long> tenants, List<DictEntryCollectionParam> dataDictEntries, boolean isExists) {
        if (isExists) {
            for (Long tenant : tenants) {
                executeWithTenant(tenant, () -> {
                    List<DataDictionary> dataDictEntities = new ArrayList<>();
                    Map<String, DataDictionaryVO> dataDictVoMap = fetchDataDictionaryIdByTypeAndCodeMap(tenant);
                    for (DictEntryCollectionParam dictEntryCollectionParam : dataDictEntries) {
                        String dictType = dictEntryCollectionParam.getDictType();
                        for (DictEntryParam entryParam : dictEntryCollectionParam.getDictEntries()) {
                            DataDictionaryVO dictEntryVo = dataDictVoMap.get(buildKey(dictType, entryParam.getKey()));
                            if (dictEntryVo != null) {
                                dataDictEntities.add(buildDataDictUpdateEntity(dictEntryVo, entryParam, tenant));
                            } else {
                                dataDictEntities.add(buildDataDictAddEntity(dictType, entryParam, tenant));
                            }
                        }
                    }
                    dataDictionaryServer.batchAddOrUpdate(dataDictEntities);
                });
            }
        } else {
            List<Long> idsToBeRemoved = new ArrayList<>();
            for (Long tenantId : tenants) {
                Map<String, DataDictionaryVO> dictIdByTypeAndCode = fetchDataDictionaryIdByTypeAndCodeMap(tenantId);
                for (DictEntryCollectionParam entryCollection : dataDictEntries) {
                    for (DictEntryParam entry : entryCollection.getDictEntries()) {
                        DataDictionaryVO vo = dictIdByTypeAndCode.get(
                                buildKey(entryCollection.getDictType(), entry.getKey()));
                        if (vo != null) {
                            idsToBeRemoved.add(vo.getId());
                        } else {
                            log.info("data dict entry not found for removal: type: {}, code: {}",
                                     entryCollection.getDictType(), entry.getKey());
                        }
                    }
                }
            }
            int removedCount = dataDictionaryServer.batchRemoveByIds(idsToBeRemoved);
            log.info("remove data dictionaries with {} entries return: {}", idsToBeRemoved.size(), removedCount);
        }
    }

    private DataDictionary buildDataDictAddEntity(String dictType, DictEntryParam entryParam, Long tenant) {
        DataDictionary dictEntry = new DataDictionary();
        dictEntry.setName(entryParam.getLabel());
        dictEntry.setCode(entryParam.getKey());
        dictEntry.setVariable(entryParam.getVariable());
        dictEntry.setRemarks(entryParam.getDescription());
        dictEntry.setVersion(VersionConstant.VERSION);
        dictEntry.setStatus(StatusEnum.ENABLE.name());
        dictEntry.setDeleted(Boolean.FALSE);
        dictEntry.setItemCode(dictType);
        dictEntry.setTenant(tenant.intValue());
        return dictEntry;
    }

    /**
     * 构建用于更新的DataDictionary 对象， 需要注意传入的dictEntryVo需要是由该tenant的实体转化而来的。
     *
     * @param dictEntryVo
     * @param entryParam
     * @param tenant
     *
     * @return
     */
    private DataDictionary buildDataDictUpdateEntity(DataDictionaryVO dictEntryVo, DictEntryParam entryParam, Long tenant) {
        DataDictionary dictEntry = dataDictionaryMapper.selectById(dictEntryVo.getId());
        // itemCode 不应该变化，不做设置
        dictEntry.setCode(entryParam.getKey());
        dictEntry.setName(entryParam.getLabel());
        dictEntry.setRemarks(entryParam.getDescription());
        dictEntry.setDeleted(Boolean.FALSE);
        dictEntry.setStatus(StatusEnum.ENABLE.name());
        dictEntry.setVariable(entryParam.getVariable());
        // 暂时未了解规则，先不做version更新
        return dictEntry;
    }

    private String buildKey(String dictType, String code) {
        return String.format(DICT_TYPE_KEY_TEMPLATE, dictType, code);
    }

    private Map<String, DataDictionaryVO> fetchDataDictionaryIdByTypeAndCodeMap(Long tenantId) {
        Map<String, DataDictionaryVO> dictEntryByTypeAndCode = new HashMap<>();
        List<DataDictionaryVO> dataDictionaryVOS = dataDictionaryServer.getByTenant(tenantId);
        for (DataDictionaryVO dataDictionaryVO : dataDictionaryVOS) {
            dictEntryByTypeAndCode.put(buildKey(dataDictionaryVO.getItemCode(), dataDictionaryVO.getCode()), dataDictionaryVO);
        }
        return dictEntryByTypeAndCode;
    }

    private Collection<DataDictionaryAddParam> convertDictEntryCollectionParam2DataDictionaryAddParams(
            DictEntryCollectionParam dataDictEntry
                                                                                                      ) {
        return dataDictEntry.getDictEntries().stream()
                .map(param -> buildDataDictionaryAddParam(dataDictEntry.getDictType(), param))
                .collect(Collectors.toList());
    }

    private static DataDictionaryAddParam buildDataDictionaryAddParam(String dictType, DictEntryParam param) {
        DataDictionaryAddParam ret = new DataDictionaryAddParam();
        ret.setItemCode(dictType);
        ret.setCode(param.getKey());
        ret.setName(param.getLabel());
        ret.setRemarks(param.getDescription());
        ret.setVariable(param.getVariable());
        ret.setVersion(VersionConstant.VERSION);
        ret.setStatus(StatusEnum.ENABLE.name());
        return ret;
    }

    private void ensureCascadeDictEntryExists(Collection<Long> tenants, List<DictEntryCollectionParam> cascadeDictEntries,
                                              boolean isExists
                                             ) {
        for (Long tenant : tenants) {
            executeWithTenant(tenant, () -> {
                Map<String, Map<String, Long>> idByCodeMapByType = fetchOrderedCascadeDictionaryIdByCodePathByType(tenant);
                if (isExists) {
                    addOrUpdateCascadeDictEntries(cascadeDictEntries, idByCodeMapByType);
                } else {
                    removeCascadeDictEntries(cascadeDictEntries, idByCodeMapByType);
                }
            });
        }
    }

    private void addOrUpdateCascadeDictEntries(List<DictEntryCollectionParam> cascadeDictEntries,
                                               Map<String, Map<String, Long>> parentMappingByType
                                              ) {
        for (DictEntryCollectionParam dictEntryCollectionParam : cascadeDictEntries) {
            String dictType = dictEntryCollectionParam.getDictType();
            Map<String, Long> parentMapping = parentMappingByType.get(dictType);
            if (parentMapping == null) {
                log.warn("dict type requested {} not exists, requested changes ignored.", dictType);
                continue;
            }
            String parentCodePath = dictEntryCollectionParam.getParent();
            Long superiorId = StringUtils.isEmpty(parentCodePath) ?
                              DictionaryConstant.SUPERIORS_ID : parentMapping.get(parentCodePath);
            if (superiorId == null) {
                log.warn("superior dict entry not exists for type: {}, codePath: {}", dictType, parentCodePath);
                continue;
            }
            // 顶层节点是level 1
            long level = Arrays.stream(parentCodePath.split(PARENT_CODE_SEPARATOR, -1))
                                 .filter(x -> !StringUtils.isEmpty(x))
                                 .count() + 1;
            for (DictEntryParam dictEntryParam : dictEntryCollectionParam.getDictEntries()) {
                String codePath = buildCodePath(dictEntryCollectionParam.getParent(), dictEntryParam.getKey());
                Long currentId = parentMapping.get(codePath);
                if (currentId == null) {
                    CascadeDictionaryAddParam addParam = buildCascadeDictionaryAddParam(dictEntryParam, dictType, superiorId, (int) level);
                    CascadeDictionaryVO created = cascadeDictionaryService.addCascadeDictionary(addParam);
                    parentMapping.put(codePath, created.getId());
                } else {
                    cascadeDictionaryService.updateCascadeDictionary(buildCascadeDictionaryUpdateParam(currentId, dictEntryParam));
                }
            }
        }
    }

    private void removeCascadeDictEntries(List<DictEntryCollectionParam> cascadeDictEntries,
                                          Map<String, Map<String, Long>> idByCodeMapByType
                                         ) {
        // 先构建待删除的项目id集合
        List<Long> toBeRemovedIds = new ArrayList<>();
        for (DictEntryCollectionParam dictEntryCollectionParam : cascadeDictEntries) {
            String dictType = dictEntryCollectionParam.getDictType();
            Map<String, Long> idByCodeMap = idByCodeMapByType.get(dictType);
            String parentCodePath = dictEntryCollectionParam.getParent();
            for (DictEntryParam dictEntryParam : dictEntryCollectionParam.getDictEntries()) {
                String codePath = buildCodePath(parentCodePath, dictEntryParam.getKey());
                Long currentId = idByCodeMap.get(codePath);
                if (currentId != null) {
                    toBeRemovedIds.add(currentId);
                } else {
                    log.warn("CascadeDictEntry with [type: {} codePath {}] not found.", dictType, dictEntryParam.getKey());
                }
            }
        }
        // 逆序后执行删除。
        Collections.reverse(toBeRemovedIds);
        int failedCount = 0;
        for (Long toBeRemovedId : toBeRemovedIds) {
            if (!cascadeDictionaryService.deleteCascadeDictionary(toBeRemovedId)) {
                ++failedCount;
            }
        }
        log.info("remove cascade dictionaries: {} failed, {} success", failedCount, toBeRemovedIds.size() - failedCount);
    }

    private CascadeDictionaryAddParam buildCascadeDictionaryAddParam(
            DictEntryParam dictEntryParam, String dictType, Long superiorId, int level) {
        CascadeDictionaryAddParam addParam = new CascadeDictionaryAddParam();
        addParam.setItemCode(dictType);
        addParam.setCode(dictEntryParam.getKey());
        addParam.setName(dictEntryParam.getLabel());
        addParam.setVariable(dictEntryParam.getVariable());
        addParam.setPermissionState(dictEntryParam.getPermState());
        addParam.setRemarks(dictEntryParam.getDescription());
        addParam.setSuperiorsId(superiorId);
        addParam.setLevel(level);
        return addParam;
    }

    private CascadeDictionaryUpdateParam buildCascadeDictionaryUpdateParam(Long currentId, DictEntryParam dictEntryParam) {
        CascadeDictionaryUpdateParam updateParam = new CascadeDictionaryUpdateParam();
        updateParam.setId(currentId);
        updateParam.setCode(dictEntryParam.getKey());
        updateParam.setName(dictEntryParam.getLabel());
        updateParam.setVariable(dictEntryParam.getVariable());
        updateParam.setRemarks(dictEntryParam.getDescription());
        // XXX: permission state 可能有修改需求
        return updateParam;
    }

    private void ensureSysDictEntryExistent(List<DictEntryCollectionParam> sysDictEntries, boolean isExists) {
        Map<String, SysDictionaryVO> sysDictByTypeAndCodeMap = fetchSysDictionaryIdByTypeAndCodeMap();
        if (isExists) {
            addOrUpdateSysDictEntries(sysDictEntries, sysDictByTypeAndCodeMap);
        } else {
            removeSysDictEntries(sysDictEntries, sysDictByTypeAndCodeMap);
        }
    }

    private void removeSysDictEntries(List<DictEntryCollectionParam> sysDictEntries, Map<String, SysDictionaryVO> dataDictVoByTypeMap) {
        List<Long> idsToRemove = new ArrayList<>();
        for (DictEntryCollectionParam dictEntryCollectionParam : sysDictEntries) {
            String dictType = dictEntryCollectionParam.getDictType();
            for (DictEntryParam entryParam : dictEntryCollectionParam.getDictEntries()) {
                SysDictionaryVO dictEntryVo = dataDictVoByTypeMap.get(buildKey(dictType, entryParam.getKey()));
                if (dictEntryVo != null) {
                    idsToRemove.add(dictEntryVo.getId());
                }
            }
        }
        int count = sysDictionaryService.batchRemoveById(idsToRemove);
        log.info("remove sys dictionaries return: {}", count);
    }

    private void addOrUpdateSysDictEntries(List<DictEntryCollectionParam> sysDictEntries, Map<String, SysDictionaryVO> dataDictVoMap) {
        List<SysDictionary> sysDictEntities = new ArrayList<>();
        for (DictEntryCollectionParam dictEntryCollectionParam : sysDictEntries) {
            String dictType = dictEntryCollectionParam.getDictType();
            for (DictEntryParam entryParam : dictEntryCollectionParam.getDictEntries()) {
                SysDictionaryVO dictEntryVo = dataDictVoMap.get(buildKey(dictType, entryParam.getKey()));
                if (dictEntryVo != null) {
                    sysDictEntities.add(buildSysDictUpdateEntity(dictEntryVo, entryParam));
                } else {
                    sysDictEntities.add(buildSysDictAddEntity(dictType, entryParam));
                }
            }
        }
        if (!sysDictionaryService.batchSaveOrUpdate(sysDictEntities)) {
            log.warn("add or update sys dictionary failed");
        }
    }

    private SysDictionary buildSysDictAddEntity(String dictType, DictEntryParam entryParam) {
        SysDictionary dictEntry = new SysDictionary();
        dictEntry.setName(entryParam.getLabel());
        dictEntry.setCode(entryParam.getKey());
        dictEntry.setRemarks(entryParam.getDescription());
        dictEntry.setVariable(entryParam.getVariable());
        dictEntry.setVersion(VersionConstant.VERSION);
        dictEntry.setStatus(StatusEnum.ENABLE.name());
        dictEntry.setDeleted(Boolean.FALSE);
        dictEntry.setItemCode(dictType);
        return dictEntry;
    }

    /**
     * 构建用于更新的DataDictionary对象， 需要注意传入的dictEntryVo需要是由该tenant的实体转化而来的。
     */
    private SysDictionary buildSysDictUpdateEntity(SysDictionaryVO dictEntryVo, DictEntryParam entryParam) {
        SysDictionary dictEntry = ConvertUtils.convert(SysDictionary.class, dictEntryVo);
        dictEntry.setCode(entryParam.getKey());
        dictEntry.setName(entryParam.getLabel());
        dictEntry.setRemarks(entryParam.getDescription());
        dictEntry.setDeleted(Boolean.FALSE);
        dictEntry.setStatus(StatusEnum.ENABLE.name());
        return dictEntry;
    }

    private Map<String, SysDictionaryVO> fetchSysDictionaryIdByTypeAndCodeMap() {
        // TODO: 关注一下已删除的是否会被查出， 已确定合并方法是否需要改进。
        return sysDictionaryService.getAll().stream().
                collect(toMap(
                        x -> buildKey(x.getItemCode(), x.getCode()),
                        Function.identity(), (o1, o2) -> o1
                             ));
    }

    private DictEntryParam convertSystemDictEntry2DictEntryParam(SysDictionaryVO sysDictionaryVO) {
        DictEntryParam dictEntryParam = new DictEntryParam();
        dictEntryParam.setKey(sysDictionaryVO.getCode());
        dictEntryParam.setLabel(sysDictionaryVO.getName());
        dictEntryParam.setVariable(sysDictionaryVO.getVariable() == null ? "" : sysDictionaryVO.getVariable());
        dictEntryParam.setDescription(sysDictionaryVO.getRemarks() == null ? "" : sysDictionaryVO.getRemarks());
        return dictEntryParam;
    }

    private DictEntryParam convertDataDict2DictEntryParam(DataDictionaryVO d) {
        DictEntryParam ret = new DictEntryParam();
        ret.setLabel(d.getName());
        ret.setKey(d.getCode());
        ret.setVariable(d.getVariable() == null ? "" : d.getVariable());
        ret.setDescription(d.getRemarks() == null ? "" : d.getRemarks());
        return ret;
    }

    private DictEntryParam convertCascadeDict2DictEntryParam(CascadeDictionary d) {
        DictEntryParam ret = new DictEntryParam();
        ret.setLabel(d.getName());
        ret.setKey(d.getCode());
        ret.setDescription(d.getRemarks() == null ? "" : d.getRemarks());
        ret.setVariable(d.getVariable() == null ? "" : d.getVariable());
        ret.setPermState(d.getPermissionState() == null ? "" : d.getPermissionState());
        return ret;
    }

    public List<DictEntryCollectionParam> fetchRuntimeDictEntryCollections(String tenantIdStr) {
        if (StringUtils.isEmpty(tenantIdStr)) { // 没有tenantId, 映射到系统字典
            return fetchSysDictEntryCollection();
        } else { // 有tenantId 就映射到数据字典和级联字典两组.
            List<DictEntryCollectionParam> dictEntryCollectionParams = new ArrayList<>();
            Long tenantId = Long.valueOf(tenantIdStr);
            dictEntryCollectionParams.addAll(fetchDataDictEntryCollection(tenantId));
            dictEntryCollectionParams.addAll(fetchCascadeDictEntryCollection(tenantId));
            return dictEntryCollectionParams;
        }
    }

    private List<DictEntryCollectionParam> fetchSysDictEntryCollection() {
        List<DictEntryCollectionParam> dictEntryCollectionParams = new ArrayList<>();
        List<SysDictionaryVO> sysDictionaryVOS = sysDictionaryService.getAll().stream()
                .sorted(Comparator.comparing(SysDictionaryVO::getCode))
                .collect(Collectors.toList());
        Map<String, List<DictEntryParam>> dictEntriesByType = new LinkedHashMap<>();
        for (SysDictionaryVO sysDictionaryVO : sysDictionaryVOS) {
            List<DictEntryParam> entries = dictEntriesByType.computeIfAbsent(
                    sysDictionaryVO.getItemCode(), (k) -> new ArrayList<>());
            entries.add(convertSystemDictEntry2DictEntryParam(sysDictionaryVO));
        }
        for (Map.Entry<String, List<DictEntryParam>> entry : dictEntriesByType.entrySet()) {
            dictEntryCollectionParams.add(DictEntryCollectionParam.builder()
                                                  .dictType(entry.getKey())
                                                  .isCascade(Boolean.FALSE)
                                                  .parent(EMPTY_PARENT_CODE)
                                                  .isSystem(Boolean.TRUE)
                                                  .dictEntries(entry.getValue())
                                                  .build());
        }
        return dictEntryCollectionParams;
    }

    private List<DictEntryCollectionParam> fetchDataDictEntryCollection(Long tenantId) {
        List<DictEntryCollectionParam> dictEntryCollectionParams = new ArrayList<>();
        List<DataDictionaryVO> dataDictionaryVOS = dataDictionaryServer.getByTenant(tenantId)
                .stream()
                .sorted(Comparator.comparing(DataDictionaryVO::getItemCode)
                                .thenComparing(DataDictionaryVO::getCode))
                .collect(Collectors.toList());
        Map<String, List<DictEntryParam>> dictEntriesByType = new LinkedHashMap<>();
        for (DataDictionaryVO dataDictionaryVO : dataDictionaryVOS) {
            List<DictEntryParam> entries = dictEntriesByType.computeIfAbsent(
                    dataDictionaryVO.getItemCode(), (k) -> new ArrayList<>());
            entries.add(convertDataDict2DictEntryParam(dataDictionaryVO));
        }
        for (Map.Entry<String, List<DictEntryParam>> entry : dictEntriesByType.entrySet()) {
            dictEntryCollectionParams.add(DictEntryCollectionParam.builder()
                                                  .dictType(entry.getKey())
                                                  .isCascade(Boolean.FALSE)
                                                  .parent(EMPTY_PARENT_CODE)
                                                  .isSystem(Boolean.FALSE)
                                                  .dictEntries(entry.getValue())
                                                  .build());
        }
        return dictEntryCollectionParams;
    }

    /**
     * 取指定tenant的级联字典项， 将每一个类型到根节点的映射填入outRootCascadeDictEntriesByType中,
     * 构建并返回由父节点id到子节点列表的映射表
     *
     * @param tenantId                        租户id
     * @param outRootCascadeDictEntriesByType 输出类型到根节点列表的map, 必须是可写的
     *
     * @return 父节点id到子节点列表的映射表
     */
    private Map<Long, List<CascadeDictionary>> fetchCascadeDictEntriesByParentIdAndBuildRootEntriesByTypeMap(
            Long tenantId, Map<String, List<CascadeDictionary>> outRootCascadeDictEntriesByType) {
        // 取回租户的全部级联字典
        Map<Long, List<CascadeDictionary>> cascadeDictEntriesByParent = new LinkedHashMap<>();
        List<CascadeDictionary> cascadeDictionaryVOS = cascadeDictionaryService.getByTenant(tenantId)
                .stream()
                .sorted(Comparator.comparing(CascadeDictionary::getItemCode)
                                .thenComparing(x -> Optional.ofNullable(x.getLevel()).orElse(Integer.valueOf(1)))
                                .thenComparing(x -> Optional.ofNullable(x.getSuperiorsId()).orElse(DictionaryConstant.SUPERIORS_ID))
                                .thenComparing(CascadeDictionary::getCode))
                .collect(Collectors.toList());
        if (cascadeDictionaryVOS.isEmpty()) {
            return cascadeDictEntriesByParent;
        }

        List<CascadeDictionary> entries;
        for (CascadeDictionary cascadeDictionary : cascadeDictionaryVOS) {
            if (cascadeDictionary.getSuperiorsId() == null
                || cascadeDictionary.getSuperiorsId().equals(DictionaryConstant.SUPERIORS_ID)) {
                // 根节点存放目标在outRootCascadeDictEntriesByType中
                entries = outRootCascadeDictEntriesByType.computeIfAbsent(cascadeDictionary.getItemCode(), k -> new ArrayList<>());
            } else {
                // 非根节点在即将返回的map中
                entries = cascadeDictEntriesByParent.computeIfAbsent(cascadeDictionary.getSuperiorsId(), (k) -> new ArrayList<>());
            }
            entries.add(cascadeDictionary);
        }
        return cascadeDictEntriesByParent;
    }

    /**
     * 获取指定tenant下已经存在的级联字典的构建基于dictType的 有序项目codepath 到id映射表。
     * 这个有序指前驱结点的key一定在后继结点之前。用于创建从前自后创建即可， 用于删除，需要由后向前删除。
     */
    private Map<String, Map<String, Long>> fetchOrderedCascadeDictionaryIdByCodePathByType(Long tenantId) {
        List<String> dictTypes = fetchDictTypes();
        Map<String, List<CascadeDictionary>> cascadeDictEntriesByType = buildEmptyRootCascadeDictEntriesByTypeMap(dictTypes);
        Map<Long, List<CascadeDictionary>> cascadeDictEntriesByParent = fetchCascadeDictEntriesByParentIdAndBuildRootEntriesByTypeMap(tenantId, cascadeDictEntriesByType);

        // 该map 存放由字典类型到该类型下 CodePath到id的map的映射表
        Map<String, Map<String, Long>> cascadeDictIdByCodePathByType = new LinkedHashMap<>(cascadeDictEntriesByType.size());
        for (Map.Entry<String, List<CascadeDictionary>> entry : cascadeDictEntriesByType.entrySet()) {
            Map<String, Long> map = cascadeDictIdByCodePathByType.computeIfAbsent(entry.getKey(), k -> new LinkedHashMap<>());
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                // 填入类型下的CodePath到id表
                map.putAll(buildCascadeDictionaryIdByCodeMap(entry.getValue(), cascadeDictEntriesByParent));
            }
        }
        return cascadeDictIdByCodePathByType;
    }

    /**
     * 基于根节点列表和父id到子节点列表映射构建CodePath到id的映射表
     *
     * @param rootEntries                根节点
     * @param cascadeDictEntriesByParent 父id到子节点映射表
     *
     * @return
     */
    private Map<String, Long> buildCascadeDictionaryIdByCodeMap(
            List<CascadeDictionary> rootEntries, Map<Long, List<CascadeDictionary>> cascadeDictEntriesByParent) {
        Map<String, Long> idByCodePath = new LinkedHashMap<>();
        Queue<CascadeDictionary> inProcessingItem = new LinkedList<>(rootEntries);
        Map<Long, String> codePathByIdMap = new HashMap<>();
        while (!inProcessingItem.isEmpty()) {
            CascadeDictionary item = inProcessingItem.poll();
            String codePath = DictionaryConstant.SUPERIORS_ID.equals(item.getSuperiorsId()) ?
                              item.getCode() : buildCodePath(codePathByIdMap.get(item.getSuperiorsId()), item.getCode());
            idByCodePath.put(codePath, item.getId());
            List<CascadeDictionary> children = cascadeDictEntriesByParent.get(item.getId());
            if (children != null && !children.isEmpty()) {
                codePathByIdMap.put(item.getId(), codePath);
                inProcessingItem.addAll(children);
            }
        }
        return idByCodePath;
    }

    private List<DictEntryCollectionParam> fetchCascadeDictEntryCollection(Long tenantId) {
        List<String> dictTypes = fetchDictTypes();
        // 存放由字典类型到该类型的根节点的条目列表的map， 初始每个type对应为空列表
        Map<String, List<CascadeDictionary>> cascadeDictEntriesByType = buildEmptyRootCascadeDictEntriesByTypeMap(dictTypes);
        // 存放从父id到子条目的map
        Map<Long, List<CascadeDictionary>> cascadeDictEntriesByParent = fetchCascadeDictEntriesByParentIdAndBuildRootEntriesByTypeMap(tenantId, cascadeDictEntriesByType);
        List<DictEntryCollectionParam> dictEntryCollectionParams = new ArrayList<>();
        // 整理级联字典项目成为基于字典类型的一级项目集和基于同一parent的兄弟项目集两个映射表
        // 从一级项目开始, BFS 遍历全部节点, 逐层级构建字典项集合.
        Map<Long, String> codePathById = new HashMap<>();
        Queue<CascadeDictionary> inProcessingItem = new LinkedList<>();
        for (Map.Entry<String, List<CascadeDictionary>> entry : cascadeDictEntriesByType.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            dictEntryCollectionParams.add(DictEntryCollectionParam.builder()
                                                  .dictType(entry.getKey())
                                                  .isCascade(Boolean.TRUE)
                                                  .isSystem(Boolean.FALSE)
                                                  .parent(EMPTY_PARENT_CODE)
                                                  .dictEntries(entry.getValue().stream()
                                                                       .map(this::convertCascadeDict2DictEntryParam)
                                                                       .collect(Collectors.toList()))
                                                  .build());
            inProcessingItem.addAll(entry.getValue());
            for (CascadeDictionary entry2 : entry.getValue()) {
                codePathById.put(entry2.getId(), entry2.getCode());
            }
        }
        while (!inProcessingItem.isEmpty()) {
            CascadeDictionary current = inProcessingItem.poll();
            Collection<CascadeDictionary> children = cascadeDictEntriesByParent.get(current.getId());
            if (children != null && !children.isEmpty()) {
                String codePath = buildCodePath(codePathById.get(current.getSuperiorsId()), current.getCode());
                dictEntryCollectionParams.add(DictEntryCollectionParam.builder()
                                                      .dictType(current.getItemCode())
                                                      .isCascade(Boolean.TRUE)
                                                      .isSystem(Boolean.FALSE)
                                                      .parent(codePath)
                                                      .dictEntries(children.stream()
                                                                           .map(this::convertCascadeDict2DictEntryParam)
                                                                           .collect(Collectors.toList()))
                                                      .build());
                inProcessingItem.addAll(children);
                for (CascadeDictionary child : children) {
                    codePathById.put(child.getId(), buildCodePath(codePath, child.getCode()));
                }
            }
        }
        // dictEntryCollectionParams内部的Entries的排序将由前面的初次获取来保证
        return dictEntryCollectionParams.stream()
                .sorted(Comparator
                                .comparing(DictEntryCollectionParam::getDictType)
                                .thenComparing(x -> Optional.ofNullable(x.getParent()).orElse("")))
                .collect(Collectors.toList());
    }

    private String buildCodePath(String parentCode, String code) {
        return StringUtils.isEmpty(parentCode) ? code : parentCode + PARENT_CODE_SEPARATOR + code;
    }

    private static Map<String, List<CascadeDictionary>> buildEmptyRootCascadeDictEntriesByTypeMap(List<String> dictTypes) {
        Map<String, List<CascadeDictionary>> cascadeDictEntriesByType = new LinkedHashMap<>(dictTypes.size());
        for (String dictType : dictTypes) {
            cascadeDictEntriesByType.put(dictType, new ArrayList<>());
        }
        return cascadeDictEntriesByType;
    }

    private DictTypeParam convertDictItemVO2DictTypeParam(DictionaryItemVO item) {
        DictTypeParam ret = new DictTypeParam();
        ret.setCode(item.getCode());
        ret.setRemark(item.getRemarks());
        return ret;
    }

    private List<String> fetchDictTypes() {
        List<DictionaryItemVO> dictionaryItems = dictionaryItemService.getDictionaryItemAll();
        return dictionaryItems.stream()
                .map(DictionaryItemVO::getCode)
                .collect(Collectors.toList());
    }

    public List<DictTypeParam> fetchRuntimeDictTypes(String tenantId) {
        List<DictionaryItemVO> dictionaryItems = dictionaryItemService.getDictionaryItemAll();
        return dictionaryItems.stream().map(this::convertDictItemVO2DictTypeParam)
                .sorted(Comparator.comparing(DictTypeParam::getCode))
                .collect(Collectors.toList());
    }
}
