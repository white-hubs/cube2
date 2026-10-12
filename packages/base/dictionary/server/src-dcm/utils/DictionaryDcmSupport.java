package com.guhecloud.cube.base.dictionary.dcm.utils;

import com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam;
import com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam.DictTypeParam;
import com.guhecloud.cube.base.dictionary.dcm.utils.DictEntryCollectionTreeAdaptor.DictEntryCollectionWrapper;
import com.guhecloud.platform.base.model.Pair;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.DcmRequirement;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import com.guhecloud.platform.dcm.support.DiffContext;
import com.guhecloud.platform.dcm.support.TreeDiff;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.CONFIG_TYPE_DICT_ENTRY;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.DictEntryParam;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.KWKEY_DICT_TYPE;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.KWKEY_IS_CASCADE;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictEntryCollectionParam.KWKEY_PARENT_CODE;
import static com.guhecloud.cube.base.dictionary.dcm.param.DictTypeCollectionParam.CONFIG_TYPE_DICT_TYPE;

@Slf4j
public class DictionaryDcmSupport {
    public static final String PROVIDER_DICT = "DICT";
    private static final String CODE_PATH_SEPARATOR = "::";

    public static RequestedConfigItem convertDictEntryCollectionParam2RequestedConfigItem(
            DictEntryCollectionParam dictEntryCollectionParam, boolean isRemoval, List<String> tenants) {

        if (dictEntryCollectionParam == null
            || CollectionUtils.isEmpty(dictEntryCollectionParam.getDictEntries())) {
            return null;
        }
        RequestedConfigItem requestedConfigItem = new RequestedConfigItem();
        requestedConfigItem.setConfigType(CONFIG_TYPE_DICT_ENTRY);
        requestedConfigItem.setVersion("1.0");
        requestedConfigItem.setIsMandatory(Boolean.TRUE);
        requestedConfigItem.setIsSystem(dictEntryCollectionParam.getIsSystem());
        requestedConfigItem.setTenants(tenants);  // XXX: 逻辑冲突由外部处理.
        requestedConfigItem.setIsRemoval(isRemoval);
        Map<String, String> kwargs = new HashMap<>();
        kwargs.put(KWKEY_DICT_TYPE, dictEntryCollectionParam.getDictType());
        kwargs.put(KWKEY_IS_CASCADE, String.valueOf(Boolean.TRUE.equals(dictEntryCollectionParam.getIsCascade())));
        kwargs.put(KWKEY_PARENT_CODE, dictEntryCollectionParam.getParent());
        requestedConfigItem.setKwargs(kwargs);
        requestedConfigItem.setVargs(new ArrayList<>(dictEntryCollectionParam.getDictEntries()));
        return requestedConfigItem;
    }

    public static RequestedConfigItem convertDictTypeParams2DcmRequestedConfigItems(
            Collection<DictTypeParam> dictTypeParams, boolean isRemoval) {
        RequestedConfigItem ret = new RequestedConfigItem();
        ret.setIsMandatory(Boolean.TRUE);
        ret.setIsSystem(Boolean.TRUE);
        ret.setIsRemoval(isRemoval);
        ret.setConfigType(CONFIG_TYPE_DICT_TYPE);
        ret.setVersion("1.0");
        ret.setVargs(dictTypeParams == null ? Collections.emptyList() : new ArrayList<>(dictTypeParams));
        return ret;
    }

    /**
     * 在指定的是否系统配置/是否指定租户配置意义下， 比较请求的配置内容与当前的配置项，
     *
     * @return 以RequestedConfigItem列表形态返回的差异
     */
    public static List<RequestedConfigItem> compare(
            List<DcmRequirement> requirements, Map<String, List<RequestedConfigItem>> existedConfigs, boolean isSystem, String tenant) {
        // existedConfigs 是当前存在的全量。
        List<DictTypeParam> existedDictTypes = existedConfigs.getOrDefault(CONFIG_TYPE_DICT_TYPE.toUpperCase(),
                                                                           Collections.emptyList())
                .stream()
                .map(x -> DictTypeCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(x)))
                .flatMap(x -> x.getDictTypeEntries().stream())
                .collect(Collectors.toList());
        List<DictEntryCollectionParam> existedDictEntries = existedConfigs.getOrDefault(CONFIG_TYPE_DICT_ENTRY.toUpperCase(),
                                                                                        Collections.emptyList())
                .stream()
                .filter(x -> CONFIG_TYPE_DICT_ENTRY.equalsIgnoreCase(x.getConfigType()))
                .map(x -> DictEntryCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(x)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<RequestedConfigItem> diffs = new ArrayList<>();
        List<RequestedConfigItem> diffsDictType = new ArrayList<>();
        List<RequestedConfigItem> diffsDictEntry = new ArrayList<>();
        for (DcmRequirement requirement : requirements) {
            if (PROVIDER_DICT.equalsIgnoreCase(requirement.getProvider())) {
                compareDictRequirements(requirement, existedDictTypes, existedDictEntries,
                                        isSystem, tenant, diffsDictType, diffsDictEntry);
            }
        }
        diffs.addAll(diffsDictType);
        diffs.addAll(diffsDictEntry);
        return diffs;
    }

    private static List<RequestedConfigItem> convertConfigItemsFromRaw2Typed(List<RequestedConfigItem> params) {
        List<RequestedConfigItem> convertedConfigItems = new ArrayList<>();
        RequestedConfigItem converted;
        for (RequestedConfigItem requestedConfigItem : params) {
            RequestedConfigItemParam requestedConfigItemParam = RequestedConfigItemParam.fromEntity(requestedConfigItem);
            converted = null;
            if (CONFIG_TYPE_DICT_TYPE.equalsIgnoreCase(requestedConfigItem.getConfigType())) {
                converted = convertDictTypeParams2DcmRequestedConfigItems(DictTypeCollectionParam.parseFrom(requestedConfigItemParam)
                                                                                  .getDictTypeEntries(), Boolean.TRUE.equals(requestedConfigItem.getIsRemoval()));
            } else if (CONFIG_TYPE_DICT_ENTRY.equalsIgnoreCase(requestedConfigItem.getConfigType())) {
                converted = convertDictEntryCollectionParam2RequestedConfigItem(DictEntryCollectionParam.parseFrom(requestedConfigItemParam), Boolean.TRUE.equals(requestedConfigItem.getIsRemoval()), requestedConfigItem.getTenants());
            }
            if (converted != null) {
                converted.setIsMandatory(requestedConfigItem.getIsMandatory());
                converted.setIsSystem(requestedConfigItem.getIsSystem());
                convertedConfigItems.add(converted);
            }
        }
        return convertedConfigItems;
    }

    private static void compareDictRequirements(
            DcmRequirement requirement, List<DictTypeParam> existedDictTypes, List<DictEntryCollectionParam> existedDictEntries,
            boolean isSystem, String tenant,
            List<RequestedConfigItem> outDiffsDictType, List<RequestedConfigItem> outDiffsDictEntry
                                               ) {
        boolean isPartialMode = Boolean.TRUE.equals(requirement.getIsPartialMode());
        // 统一转换vargs, 便于后续处理逻辑能简便地以统一的样式进行处理。
        List<RequestedConfigItem> requestedConfigItems = convertConfigItemsFromRaw2Typed(requirement.getRequirements());
        List<RequestedConfigItem> toBeProcessed = requestedConfigItems.stream()
                .filter(x -> isRequestConfigCompatibleWith(x, isSystem, tenant))
                .collect(Collectors.toList());
        // 分类处理
        if (!isSystem) {
            // 非系统字典包含级联和非级联
            List<RequestedConfigItem> cascadeConfigItems = new ArrayList<>();
            List<RequestedConfigItem> dataDictConfigItems = new ArrayList<>();
            toBeProcessed.forEach(x -> {
                if (isRequestConfigCascade(x)) {
                    cascadeConfigItems.add(x);
                } else {
                    dataDictConfigItems.add(x);
                }
            });
            outDiffsDictEntry.addAll(
                    compareCascadeDictEntries(
                            cascadeConfigItems, existedDictEntries, isPartialMode, Collections.singletonList(tenant)));
            outDiffsDictEntry.addAll(
                    compareNonCascadeDictEntries(
                            dataDictConfigItems, existedDictEntries, isPartialMode, Collections.singletonList(tenant)));
        } else {
            // 系统字典含字典类型和系统字典
            List<RequestedConfigItem> dictTypeConfigItems = new ArrayList<>();
            List<RequestedConfigItem> sysDictConfigItems = new ArrayList<>();
            toBeProcessed.forEach(x -> {
                if (CONFIG_TYPE_DICT_TYPE.equalsIgnoreCase(x.getConfigType())) {
                    dictTypeConfigItems.add(x);
                } else if (CONFIG_TYPE_DICT_ENTRY.equalsIgnoreCase(x.getConfigType())) {
                    sysDictConfigItems.add(x);
                }
            });
            outDiffsDictType.addAll(
                    compareDictTypes(dictTypeConfigItems, existedDictTypes, isPartialMode));
            outDiffsDictEntry.addAll(
                    compareNonCascadeDictEntries(sysDictConfigItems, existedDictEntries,
                                                 isPartialMode, Collections.emptyList()));
        }

    }

    /**
     * 比较非级联字典项的请求配置项， 生成必要的配置项.
     *
     * @param dataDictConfigItems 请求配置项
     * @param existedDictEntries  存在的字典项，可能有超出范围的部分， 将依据tenants和是否cascade进行过滤。
     * @param isPartialMode       是否为部分模式
     * @param tenants             应用的租户列表。 如果租户列表为空，意味着针对系统配置部分进行处理。
     */
    @SneakyThrows
    private static List<RequestedConfigItem> compareNonCascadeDictEntries(List<RequestedConfigItem> dataDictConfigItems,
                                                                          List<DictEntryCollectionParam> existedDictEntries,
                                                                          boolean isPartialMode, List<String> tenants
                                                                         ) {
        boolean isSystem = tenants == null || tenants.isEmpty();

        List<RequestedConfigItem> outDiffsDictEntry = new ArrayList<>();
        dataDictConfigItems = dataDictConfigItems.stream()
                .filter(x -> !isRequestConfigCascade(x))
                .collect(Collectors.toList());
        Map<String, DictEntryCollectionParam> existedDictEntriesById = existedDictEntries.stream()
                .filter(x -> !Boolean.TRUE.equals(x.getIsCascade()) && Boolean.TRUE.equals(x.getIsSystem()) == isSystem)
                .collect(Collectors.toMap(DictEntryCollectionParam::getId, Function.identity()));
        if (!isPartialMode && dataDictConfigItems.isEmpty()) {
            return existedDictEntriesById.values().stream()
                    .map(x -> convertDictEntryCollectionParam2RequestedConfigItem(x, true, tenants))
                    .collect(Collectors.toList());
        }
        for (RequestedConfigItem requestedConfigItem : dataDictConfigItems) {
            if (isSystem != Boolean.TRUE.equals(requestedConfigItem.getIsSystem())) {
                continue;
            }
            boolean isRemove = Boolean.TRUE.equals(requestedConfigItem.getIsRemoval());
            DictEntryCollectionParam requestedDictEntry = DictEntryCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(requestedConfigItem));
            Map<String, DictEntryParam> toMap = requestedDictEntry.getDictEntries().stream()
                    .collect(Collectors.toMap(DictEntryParam::getId, Function.identity(),
                                              (x1, x2) -> {
                                                  log.warn("entries with duplicated key:{}, {}, {}", x1.getId(), x1, x2);
                                                  throw new ServiceException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                                                             String.format("duplicated dictionary entry key: %s, type: %s, parent: %s", x1.getId(), requestedConfigItem.getConfigType(), requestedConfigItem.getKwargs(KWKEY_PARENT_CODE, "")));
                                              }));
            DictEntryCollectionParam existedItem = existedDictEntriesById.get(requestedDictEntry.getId());
            if (isRemove) {
                if (existedItem != null) {
                    Map<String, DictEntryParam> existedMap = existedItem.getDictEntries().stream()
                            .collect(Collectors.toMap(DictEntryParam::getId, Function.identity()));
                    RequestedConfigItem rci = generateRemovalRci(requestedConfigItem, toMap, existedMap, tenants);
                    if (rci != null) {
                        outDiffsDictEntry.add(rci);
                    }
                }
            } else {
                if (existedItem == null) {
                    outDiffsDictEntry.add(requestedConfigItem);
                } else {
                    Map<String, DictEntryParam> existedMap = existedItem.getDictEntries().stream()
                            .collect(Collectors.toMap(DictEntryParam::getId, Function.identity()));
                    outDiffsDictEntry.addAll(generateAddOrUpdateRci(requestedConfigItem, toMap, existedMap, isPartialMode, tenants));
                }
            }
        }
        return outDiffsDictEntry;
    }

    private static List<RequestedConfigItem> generateAddOrUpdateRci(RequestedConfigItem requestedConfigItem,
                                                                    Map<String, ? extends Identifiable> toMap,
                                                                    Map<String, ? extends Identifiable> existedMap, boolean isPartialMode,
                                                                    List<String> tenants
                                                                   ) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        List<Object> newOrUpdated = new ArrayList<>();
        List<Object> deleted = new ArrayList<>();
        for (Map.Entry<String, ? extends Identifiable> toEntry : toMap.entrySet()) {
            Identifiable existedEntry = existedMap.get(toEntry.getKey());
            if (existedEntry == null || !Objects.equals(toEntry.getValue().getFingerPrint(), existedEntry.getFingerPrint())) {
                newOrUpdated.add(toEntry.getValue());
            }
        }
        if (!isPartialMode) {
            for (Map.Entry<String, ? extends Identifiable> existedEntry : existedMap.entrySet()) {
                if (toMap.get(existedEntry.getKey()) == null) {
                    deleted.add(existedEntry.getValue());
                }
            }
        }
        if (!newOrUpdated.isEmpty()) {
            ret.add(cloneRciAndSetParams(requestedConfigItem, tenants, newOrUpdated, Boolean.FALSE));
        }
        if (!deleted.isEmpty()) {
            ret.add(cloneRciAndSetParams(requestedConfigItem, tenants, deleted, Boolean.TRUE));
        }
        return ret;
    }

    /**
     * 对移除类型的配置申请， 校验 toMap/fromMap 中的entry， 将不必要的移除项去除， 生成必要的配置项。如果已经不需要， 返回null
     */
    private static RequestedConfigItem generateRemovalRci(RequestedConfigItem requestedConfigItem,
                                                          Map<String, ? extends Identifiable> toMap,
                                                          Map<String, ? extends Identifiable> existedMap, List<String> tenants
                                                         ) {
        List<Object> toBeDeleted = new ArrayList<>();
        RequestedConfigItem rci = null;
        for (Map.Entry<String, ? extends Identifiable> toEntry : toMap.entrySet()) {
            if (existedMap.containsKey(toEntry.getKey())) {
                toBeDeleted.add(toEntry.getValue());
            }
        }
        if (!toBeDeleted.isEmpty()) {
            rci = cloneRciAndSetParams(requestedConfigItem, tenants, toBeDeleted, Boolean.TRUE);
        }
        return rci;
    }

    @SneakyThrows
    private static RequestedConfigItem cloneRciAndSetParams(RequestedConfigItem requestedConfigItem, List<String> tenants,
                                                            List<Object> vargs, Boolean isRemoval
                                                           ) {
        RequestedConfigItem rci = (RequestedConfigItem) requestedConfigItem.clone();
        rci.setVargs(vargs);
        rci.setIsRemoval(isRemoval);
        rci.setTenants(tenants);
        return rci;
    }

    /**
     * 比较字典类型配置，生成配置项
     */
    private static List<RequestedConfigItem> compareDictTypes(List<RequestedConfigItem> dictTypeConfigItems,
                                                              List<DictTypeParam> existedDictTypes, boolean isPartialMode
                                                             ) {
        List<RequestedConfigItem> outDiffsDictEntry = new ArrayList<>();
        if (!isPartialMode && dictTypeConfigItems.isEmpty()) {
            return Collections.singletonList(
                    convertDictTypeParams2DcmRequestedConfigItems(existedDictTypes, true));
        }

        Map<String, DictTypeParam> existedMap = existedDictTypes.stream()
                .collect(Collectors.toMap(DictTypeParam::getId, Function.identity()));

        for (RequestedConfigItem requestedConfigItem : dictTypeConfigItems) {
            boolean isRemove = Boolean.TRUE.equals(requestedConfigItem.getIsRemoval());
            DictTypeCollectionParam requestedDictType = DictTypeCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(requestedConfigItem));
            Map<String, DictTypeParam> toMap = requestedDictType.getDictTypeEntries().stream()
                    .collect(Collectors.toMap(DictTypeParam::getId, Function.identity()));
            if (isRemove) {
                RequestedConfigItem rci = generateRemovalRci(requestedConfigItem, toMap, existedMap,
                                                             Collections.emptyList());
                if (rci != null) {
                    outDiffsDictEntry.add(rci);
                }
            } else {
                outDiffsDictEntry.addAll(generateAddOrUpdateRci(requestedConfigItem, toMap, existedMap,
                                                                isPartialMode, Collections.emptyList()));
            }
        }
        return outDiffsDictEntry;
    }

    /**
     * 比较申请项目和
     */
    private static List<RequestedConfigItem> compareCascadeDictEntries(
            List<RequestedConfigItem> requestedConfigItems, List<DictEntryCollectionParam> existedDictEntries,
            boolean isPartialMode, List<String> tenants
                                                                      ) {
        requestedConfigItems = requestedConfigItems.stream()
                .filter(DictionaryDcmSupport::isRequestConfigCascade)
                .collect(Collectors.toList());
        Map<String, List<DictEntryCollectionParam>> existedDictEntriesByType = new HashMap<>();
        for (DictEntryCollectionParam x : existedDictEntries) {
            if (Boolean.TRUE.equals(x.getIsCascade())) {
                List<DictEntryCollectionParam> dictEntryCollectionParams = new ArrayList<>();
                dictEntryCollectionParams.add(x);
                existedDictEntriesByType.merge(x.getDictType(), dictEntryCollectionParams, DictionaryDcmSupport::listMerger);
            }
        }
        if (!isPartialMode && requestedConfigItems.isEmpty()) {
            return existedDictEntriesByType.values().stream()
                    .flatMap(Collection::stream)
                    .map(x -> convertDictEntryCollectionParam2RequestedConfigItem(x, true, tenants))
                    .collect(Collectors.toList());
        }

        Map<String, DictEntryCollectionTreeAdaptor> existedDictEntriesTreeByType
                = convertDictEntriesByType2DictTreeAdaptorByType(existedDictEntriesByType);

        if (isPartialMode) {
            // Request Item 不一定能形成一颗完整的树， 逐一确定是否有patch, 添加到输出当中
            return requestedConfigItems.stream().map(x -> generatePartialPatchForCascadeDictTree(x, existedDictEntriesTreeByType, tenants))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            // 构建完整的树， 进行树形比较。
            Map<String, DictEntryCollectionTreeAdaptor> requestedTreeByType
                    = buildDictEntriesTreeByTypeFromRequestConfigs(requestedConfigItems);
            return generateFullPatchForCascadeTree(requestedTreeByType, existedDictEntriesTreeByType, tenants);
        }
    }

    @SneakyThrows
    public static RequestedConfigItem generatePartialPatchForCascadeDictTree(RequestedConfigItem requestedConfigItem,
                                                                             Map<String, DictEntryCollectionTreeAdaptor> existedDictEntriesTreeByType,
                                                                             List<String> tenants
                                                                            ) {
        boolean isRemove = Boolean.TRUE.equals(requestedConfigItem.getIsRemoval());
        String dictType = requestedConfigItem.getKwargs(KWKEY_DICT_TYPE, null);
        Objects.requireNonNull(dictType);
        DictEntryCollectionParam dictEntryCollectionParam = convertRequestedConfigItem2DictEntryCollectionParam(requestedConfigItem);
        DictEntryCollectionTreeAdaptor existedTree = existedDictEntriesTreeByType.get(dictType);

        if (existedTree == null) {
            if (isRemove) {
                return null;
            }
            requestedConfigItem = (RequestedConfigItem) requestedConfigItem.clone();
            requestedConfigItem.setTenants(tenants);
            return requestedConfigItem;
        }
        // XXX: 注意观察
        String targetCodePath = requestedConfigItem.getKwargs(KWKEY_PARENT_CODE, "");
        List<String> codePathInParts = Arrays.asList(targetCodePath.split(CODE_PATH_SEPARATOR, -1));
        Collection<Identifiable> existedChildren = (Collection<Identifiable>) existedTree.getChildren(codePathInParts);
        // 对于已经存在的项目，如果出现重复，仅给出告警，不立即失败
        Map<String, Identifiable> existedMap = existedChildren.stream().collect(
                Collectors.toMap(Identifiable::getId, Function.identity(), (x1, x2) -> {
                    log.warn("duplicated entity existed: {}, item: {}, {}", x1.getId(), x1, x2);
                    return x1;
                }));
        List<Object> differentItems = new ArrayList<>();
        for (DictEntryParam requestedItem : dictEntryCollectionParam.getDictEntries()) {
            Identifiable existed = existedMap.get(requestedItem.getId());
            if (!isRemove) {
                if (existed == null || !Objects.equals(existed.getFingerPrint(), requestedItem.getFingerPrint())) {
                    differentItems.add(requestedItem);
                }
            } else {
                if (existed != null) {
                    differentItems.add(requestedItem);
                }
            }
        }
        if (differentItems.isEmpty()) {
            return null;
        } else {
            RequestedConfigItem reduced = (RequestedConfigItem) requestedConfigItem.clone();
            reduced.setVargs(differentItems);
            reduced.setTenants(tenants);
            return reduced;
        }
    }

    /**
     * 针对本设计(字典类型->字典项树)的全树对比.
     *
     * @param toTreeByType
     * @param fromTreeByType
     * @param tenants
     *
     * @return
     */
    private static List<RequestedConfigItem> generateFullPatchForCascadeTree(
            Map<String, DictEntryCollectionTreeAdaptor> toTreeByType,
            Map<String, DictEntryCollectionTreeAdaptor> fromTreeByType,
            List<String> tenants
                                                                            ) {
        List<RequestedConfigItem> diffs = new ArrayList<>();
        for (Map.Entry<String, DictEntryCollectionTreeAdaptor> toEntry
                : toTreeByType.entrySet()) {
            if (!fromTreeByType.containsKey(toEntry.getKey())) {
                for (DictEntryCollectionParam x : toEntry.getValue().getAllDictEntryCollectionParams()) {
                    diffs.add(convertDictEntryCollectionParam2RequestedConfigItem(x, false, tenants));
                }
            } else {
                DiffContext diff = TreeDiff.diff(fromTreeByType.get(toEntry.getKey()), toEntry.getValue());
                if (diff.hasChanges()) {
                    diffs.addAll(convertCascadeDiffContext2RequestedConfigItem(diff, tenants));
                }
            }
        }
        for (Map.Entry<String, DictEntryCollectionTreeAdaptor> entry : fromTreeByType.entrySet()) {
            if (!toTreeByType.containsKey(entry.getKey())) {
                for (DictEntryCollectionParam x : entry.getValue().getAllDictEntryCollectionParams()) {
                    diffs.add(convertDictEntryCollectionParam2RequestedConfigItem(x, true, tenants));
                }
            }
        }
        return diffs;
    }

    private static Map<String, DictEntryCollectionTreeAdaptor> buildDictEntriesTreeByTypeFromRequestConfigs(
            List<RequestedConfigItem> requestedConfigs
                                                                                                           ) {
        Map<String, List<DictEntryCollectionParam>> requestedDictEntriesByType = new HashMap<>();
        for (RequestedConfigItem x : requestedConfigs) {
            if (Boolean.TRUE.equals(x.getIsRemoval())) {
                log.warn("non-partial(full) mode with removal: {}", x);
                continue;
            }
            List<DictEntryCollectionParam> params = new ArrayList<>();
            params.add(convertRequestedConfigItem2DictEntryCollectionParam(x));
            requestedDictEntriesByType.merge(x.getKwargs(KWKEY_DICT_TYPE, "_UNKNOWN_"),
                                             params,
                                             DictionaryDcmSupport::listMerger
                                            );
        }
        return convertDictEntriesByType2DictTreeAdaptorByType(requestedDictEntriesByType);
    }

    /**
     * 获取父级codePath
     */
    private static String getParentCodePath(String codePath) {
        int pos = codePath.lastIndexOf(CODE_PATH_SEPARATOR);
        // 不含分隔符视同顶层元素,parentCodePath为空
        return pos == -1 ? "" : codePath.substring(0, pos);
    }

    /**
     * 转换DictContext 为变更项目。
     */
    @SneakyThrows
    public static Collection<RequestedConfigItem> convertCascadeDiffContext2RequestedConfigItem(
            DiffContext<DictEntryCollectionWrapper> diff, List<String> tenants) {

        if (!diff.hasChanges()) {
            return Collections.emptyList();
        }

        List<RequestedConfigItem> requestedConfigItems = new ArrayList<>();
        Map<String, List<Object>> addOrUpdateByParentCodePath = new HashMap<>();
        Map<String, List<Object>> removeByParentCodePath = new HashMap<>();

        String dictType = null;
        for (DictEntryCollectionWrapper addNode : diff.getAddedNodes()) {
            dictType = addNode.getWrapped().getDictType();
            List<Object> params = new ArrayList<>();
            params.add(addNode.getNode());
            addOrUpdateByParentCodePath.merge(getParentCodePath(addNode.getCodePath()),
                                              params, DictionaryDcmSupport::listMerger);
        }
        for (Pair<DictEntryCollectionWrapper, DictEntryCollectionWrapper> modifiedNode : diff.getModifiedNodes()) {
            dictType = modifiedNode.getValue().getWrapped().getDictType();
            List<Object> params = new ArrayList<>();
            params.add(modifiedNode.getValue().getNode());
            addOrUpdateByParentCodePath.merge(getParentCodePath(modifiedNode.getValue().getCodePath()),
                                              params, DictionaryDcmSupport::listMerger);
        }
        for (DictEntryCollectionWrapper removeNode : diff.getDeletedNodes()) {
            dictType = removeNode.getWrapped().getDictType();
            List<Object> params = new ArrayList<>();
            params.add(removeNode.getNode());
            removeByParentCodePath.merge(getParentCodePath(removeNode.getCodePath()),
                                         params, DictionaryDcmSupport::listMerger);
        }

        RequestedConfigItem example = new RequestedConfigItem();
        example.setConfigType(CONFIG_TYPE_DICT_ENTRY);
        example.setVersion("1.0");
        example.setIsMandatory(Boolean.TRUE);
        example.setIsSystem(Boolean.FALSE);
        example.setKwargs(KWKEY_DICT_TYPE, dictType);
        example.setKwargs(KWKEY_IS_CASCADE, Boolean.TRUE.toString());

        // 构建并返回变更清单
        RequestedConfigItem rci;
        for (Map.Entry<String, List<Object>> entry : addOrUpdateByParentCodePath.entrySet()) {
            rci = cloneRciAndSetParams(example, tenants, entry.getValue(), Boolean.FALSE);
            rci.setKwargs(KWKEY_PARENT_CODE, entry.getKey());
            requestedConfigItems.add(rci);
        }
        for (Map.Entry<String, List<Object>> entry : removeByParentCodePath.entrySet()) {
            rci = cloneRciAndSetParams(example, tenants, entry.getValue(), Boolean.TRUE);
            rci.setKwargs(KWKEY_PARENT_CODE, entry.getKey());
            requestedConfigItems.add(rci);
        }
        return requestedConfigItems;
    }

    private static Map<String, DictEntryCollectionTreeAdaptor> convertDictEntriesByType2DictTreeAdaptorByType(
            Map<String, List<DictEntryCollectionParam>> existedDictEntriesByType
                                                                                                             ) {
        Map<String, DictEntryCollectionTreeAdaptor> existedDictEntriesTree = new HashMap<>();
        for (Map.Entry<String, List<DictEntryCollectionParam>> x : existedDictEntriesByType.entrySet()) {
            existedDictEntriesTree.put(x.getKey(), new DictEntryCollectionTreeAdaptor(x.getValue()));
        }
        return existedDictEntriesTree;
    }

    private static DictEntryCollectionParam convertRequestedConfigItem2DictEntryCollectionParam(RequestedConfigItem x) {
        return DictEntryCollectionParam.parseFrom(RequestedConfigItemParam.fromEntity(x));
    }

    private static boolean isRequestConfigCompatibleWith(RequestedConfigItem configItem, boolean isSystem, String tenant) {
        return (isSystem && Boolean.TRUE.equals(configItem.getIsSystem()))
               || (!isSystem && configItem.matchedWithTenant(tenant));
    }

    private static boolean isRequestConfigCascade(RequestedConfigItem configItem) {
        return !Boolean.TRUE.equals(configItem.getIsSystem()) &&
               Boolean.TRUE.equals(Boolean.parseBoolean(configItem.getKwargs(KWKEY_IS_CASCADE, "FALSE")));
    }

    private static <T> List<T> listMerger(List<T> oldValue, List<T> newValue) {
        oldValue.addAll(newValue);
        return oldValue;
    }
}
