package com.guhecloud.platform.dcm.support;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.database.entity.Entity;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.DcmApplyContext;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class DcmSupport {

    /**
     * 一维 RCI 比较
     * <ul>
     *     <li>一维是指传入的RCI 转换为DCM 配置实体后，配置实体的getId 能在配置项范围内唯一标识对象。</li>
     *     <li>部分更新模式为False时， 对于toState中不存在的非删除项目，如果fromState存在将将产生删除差异项</li>
     * </ul>
     *
     * @param toState   目标状态
     * @param fromState 现有状态
     * @param isPartial 是否以部分更新模式工作
     * @param converter 转换器
     *
     * @return 差异项
     */
    public static <DE extends Identifiable> List<RequestedConfigItem> compare1dRCIs(
            List<RequestedConfigItem> toState, List<RequestedConfigItem> fromState,
            boolean isPartial, IDcmRciConverter<DE> converter
                                                                                   ) {
        if (toState == null || toState.isEmpty()) {
            if (!isPartial) {
                // 快捷路径： 全部移除
                return fromState.stream().map(
                        x -> {
                            RequestedConfigItem rci;
                            try {
                                rci = (RequestedConfigItem) x.clone();
                            } catch (CloneNotSupportedException e) {
                                // This should never happen.
                                throw new RuntimeException(e);
                            }
                            rci.setIsRemoval(Boolean.TRUE);
                            return rci;
                        }
                                             ).collect(Collectors.toList());
            } else {
                // Partial 模式下，如果 toState 为空，则返回空集合
                return Collections.emptyList();
            }
        }
        if (fromState == null) {
            fromState = Collections.emptyList();
        }

        // 对添加项目去重. 添加不是幂等的, 需要先进行去重, 而删除天生就是幂等, 可以不进行本操作.
        LinkedHashMap<String, DE> deduplicateMap = toState.stream()
                .filter(x -> !Boolean.TRUE.equals(x.getIsRemoval()))
                .flatMap(x -> converter.convertRci2Dcm(x).stream())
                .collect(Collectors.toMap(Identifiable::getId, Function.identity(),
                                          (oldValue, newValue) -> {
                                              log.debug("adding of duplicate dcm entity found, old value used. old: {}, new: {}",
                                                        oldValue, newValue);
                                              return oldValue;
                                          }, LinkedHashMap::new));
        List<DE> toAddDcmEntities = new ArrayList<>(deduplicateMap.values());
        List<DE> toRemoveDcmEntities = toState.stream()
                .filter(x -> Boolean.TRUE.equals(x.getIsRemoval()))
                .flatMap(x -> converter.convertRci2Dcm(x).stream())
                .collect(Collectors.toList());

        Map<String, DE> existedDcmEntityByKey = fromState.stream()
                .flatMap(x -> converter.convertRci2Dcm(x).stream())
                .collect(Collectors.toMap(DE::getId, Function.identity(),
                        /* 这里容许了现有项目重复, 但需要关注日志. */
                                          (oldValue, newValue) -> {
                                              log.debug("duplicate dcm entity found in existed items, old value used. old: {}, new: {}",
                                                        oldValue, newValue);
                                              return oldValue;
                                          },
                                          LinkedHashMap::new));

        List<DE> kept2AddDcmEntities = toAddDcmEntities.stream()
                .filter(x -> existedDcmEntityByKey.get(x.getId()) == null
                             || existedDcmEntityByKey.get(x.getId()) != null
                                && !Objects.equals(existedDcmEntityByKey.get(x.getId()).getFingerPrint(), x.getFingerPrint()))
                .collect(Collectors.toList());

        Map<String, DE> toRemoveDcmEntityById = toRemoveDcmEntities.stream()
                .filter(x -> x != null && existedDcmEntityByKey.containsKey(x.getId()))
                .collect(Collectors.toMap(DE::getId, Function.identity(), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        if (!isPartial) {
            Set<String> toAddDcmEntityKeys = toAddDcmEntities.stream().map(DE::getId).collect(Collectors.toSet());
            for (Map.Entry<String, DE> entry : existedDcmEntityByKey.entrySet()) {
                if (!toAddDcmEntityKeys.contains(entry.getKey())) {
                    toRemoveDcmEntityById.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }

        List<RequestedConfigItem> ret = new ArrayList<>(converter.convertDcm2Rci(kept2AddDcmEntities));
        Collection<RequestedConfigItem> toRemoveRcis = converter.convertDcm2Rci(toRemoveDcmEntityById.values());
        toRemoveRcis.forEach(x -> x.setIsRemoval(Boolean.TRUE));
        ret.addAll(toRemoveRcis);
        //XXX: 更新全部的Tenants和IsSystems
        RequestedConfigItem rciExample = toState.get(0);
        ret.forEach(x -> {
            x.setIsSystem(rciExample.getIsSystem());
            x.setTenants(rciExample.getTenants());
        });
        return ret;
    }

    /**
     * 比较明确的请求配置项和当前存在的业务实体， 生成用于后续操作的 DcmApplyContext对象
     * 一维的概念同 compara1dRCIs
     *
     * @param requestedConfigItems 请求的配置操作
     * @param existedBizEntities   当前存在的业务实体
     * @param context              上下文实体，如果传入null，将生成新的 DcmApplyContext 对象
     * @param helper               用于比较的转换器
     *
     * @return 一个 DcmApplyContext 对象， 包含需要添加、更新、删除的DCM实体与对应的业务实体
     */
    public static <BE, DE extends Identifiable> DcmApplyContext<BE, DE> apply1dRCIs(List<RequestedConfigItem> requestedConfigItems,
                                                                                    List<BE> existedBizEntities,
                                                                                    DcmApplyContext<BE, DE> context,
                                                                                    IDcmHelper<BE, DE> helper
                                                                                   ) {
        if (context == null) {
            context = DcmApplyContext.create();
        }
        List<DE> dcmEntities2AddOrUpdate = new ArrayList<>();
        List<DE> dcmEntities2Remove = new ArrayList<>();
        for (RequestedConfigItem requestedConfigItem : requestedConfigItems) {
            if (Boolean.TRUE.equals(requestedConfigItem.getIsRemoval())) {
                dcmEntities2Remove.addAll(helper.convertRci2Dcm(requestedConfigItem));
            } else {
                dcmEntities2AddOrUpdate.addAll(helper.convertRci2Dcm(requestedConfigItem));
            }
        }
        Map<String, BE> existedEntitiesByBizKey = new HashMap<>();
        for (BE existedBizEntity : existedBizEntities) {
            try {
                String bizKey = helper.getBizKey(existedBizEntity);
                BE prevBe = existedEntitiesByBizKey.putIfAbsent(bizKey, existedBizEntity);
                if (prevBe != null) {
                    log.warn("multiple entry with same key: {}, entity1: {}, entity2: {}",
                             bizKey, prevBe, existedBizEntity);
                }
            } catch (RuntimeException re) {
                log.info("entity ignored: {}, error: {}", existedBizEntities, re.toString());
            }
        }

        BE existedEntity;
        boolean isTreeStyle = helper.isTreeStyle();
        for (DE dcmEntity : dcmEntities2AddOrUpdate) {
            existedEntity = existedEntitiesByBizKey.get(helper.getDcmKey(dcmEntity));
            try {
                if (existedEntity != null) {
                    // 前面的处理应该已经过滤了fingerprint相同的记录
                    helper.updateBizWithDcm(existedEntity, dcmEntity);
                    context.getBizEntitiesToUpdate().add(existedEntity);
                    context.getDcmEntitiesToUpdate().add(dcmEntity);
                } else {
                    // 对于树形结构， 没有执行实际的add操作， 不能构建出完整携带dcm信息的业务实体。
                    if (!isTreeStyle) {
                        BE be = helper.convertDcm2Biz(dcmEntity);
                        if (be != null) {
                            context.getBizEntitiesToAdd().add(be);
                        } else {
                            log.warn("failed to convert for adding {}.", dcmEntity);
                        }
                    }
                    context.getDcmEntitiesToAdd().add(dcmEntity);
                }
            } catch (RuntimeException re) {
                log.warn("add/update entity ignored: {}, error: {}", dcmEntity, re.toString());
            }
        }

        for (DE dcmEntity : dcmEntities2Remove) {
            existedEntity = existedEntitiesByBizKey.get(helper.getDcmKey(dcmEntity));
            if (existedEntity != null) {
                context.getBizEntitiesToRemove().add(existedEntity);
                context.getDcmEntitiesToRemove().add(dcmEntity);
            } else {
                log.debug("remove un-existed items ignored. {}", dcmEntity);
            }
        }
        return context;
    }

    /**
     * 获取运行时配置项的实一个实现辅助
     */
    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> fetchRuntimeConfig(IService<T> service, IDcmHelper<T, DE> dcmHelper) {
        return new ArrayList<>(dcmHelper.convertBiz2Rci(service.list()));
    }

    private static void setTenant(Long tenant) {
        Requester prev = SessionContextHolder.getRequester();
        SessionContextHolder.setRequester(Requester.builder()
                                                  .tenant(tenant == null ? null : tenant.intValue())
                                                  .terminal(prev.getTerminal())
                                                  .authorization(prev.getAuthorization())
                                                  .domain(prev.getDomain())
                                                  .host(prev.getHost())
                                                  .permission(prev.getPermission())
                                                  .uri(prev.getUri())
                                                  .version(prev.getVersion())
                                                  .build());
    }

    private static Long getCurrentTenant() {
        Integer currentTenant = SessionContextHolder.getRequester().getTenant();
        return currentTenant == null ? null : Long.valueOf(currentTenant);
    }

    /**
     * 在指定的租户下执行的辅助类。 注意 Runnable 用涉及到的代码的执行时机
     *
     * @param tenant
     * @param runnable
     */
    public static void executeWithTenant(Long tenant, Runnable runnable) {
        Long oldTenant = getCurrentTenant();
        try {
            if (!Objects.equals(oldTenant, tenant)) {
                setTenant(tenant);
            }
            runnable.run();
        } catch (RuntimeException re) {
            log.warn("unexpected runtime exception: {}", re.toString());
            throw re;   // rethrow
        } finally {
            if (!Objects.equals(oldTenant, tenant)) {
                setTenant(oldTenant);
            }
        }
    }

    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> fetchTenantedRuntimeConfig(
            Long tenant, IService<T> service, IDcmHelper<T, DE> dcmHelper) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenant, () -> {
            ret.addAll(fetchRuntimeConfig(service, dcmHelper));
        });
        return ret;
    }

    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> fetchRuntimeConfig(Collection<T> entities, IDcmHelper<T, DE> dcmHelper) {
        return new ArrayList<>(dcmHelper.convertBiz2Rci(entities));
    }

    /**
     * 以指定的tenant 上下文获取运行时配置.  本方法需要注意传递的参数的构造并没有再指定的租户下获取.
     * 故此使用时需要确认参数构成不依赖于指定租户上下文. 否则仍然建议使用executeWithTenant + fetchRuntimeConfig
     */
    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> fetchTenantedRuntimeConfig(Long tenantId, Collection<T> entities,
                                                         IDcmHelper<T, DE> dcmHelper
                                                        ) {
        List<RequestedConfigItem> ret = new ArrayList<>();
        executeWithTenant(tenantId, () -> {
            ret.addAll(fetchRuntimeConfig(entities, dcmHelper));
        });
        return ret;
    }

    private static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> applyDcmConfigInner(List<RequestedConfigItem> diffs2Apply,
                                                  Supplier<List<T>> existedBizEntitiesSupplier,
                                                  BiFunction<Collection<T>, Collection<DE>, Integer> batchAdder,
                                                  BiFunction<Collection<T>, Collection<DE>, Integer> batchUpdater,
                                                  BiFunction<Collection<T>, Collection<DE>, Integer> batchRemover,
                                                  IDcmHelper<T, DE> dcmHelper
                                                 ) {
        if (diffs2Apply == null || diffs2Apply.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> existedBizEntities = existedBizEntitiesSupplier.get();
        DcmApplyContext<T, DE> context = apply1dRCIs(diffs2Apply, existedBizEntities, null, dcmHelper);
        int updateCnt = 0, addCnt = 0, removeCnt = 0;
        if (context.isRemoveNeeded()) {
            removeCnt = batchRemover.apply(context.getBizEntitiesToRemove(), context.getDcmEntitiesToRemove());
        }
        if (context.isUpdateNeeded()) {
            updateCnt = batchUpdater.apply(context.getBizEntitiesToUpdate(), context.getDcmEntitiesToUpdate());
        }
        if (context.isAddNeeded()) {
            addCnt = batchAdder.apply(context.getBizEntitiesToAdd(), context.getDcmEntitiesToAdd());
        }
        log.info("apply dcm config success, RCIs: {}, Business Entities: {} added, {} updated, {} removed",
                 diffs2Apply.size(), addCnt, updateCnt, removeCnt);
        return diffs2Apply;
    }

    /**
     * 应用变更项目<br>
     * 这是最基础的版本，其高效正确工作依赖于以下条件：<br>
     * 1. bizService 的list方法仅加载必要的业务实体， 避免加载全部业务实体。<br>
     *
     * @param diffs2Apply 待应用的变更项目列表
     * @param bizService  业务实体服务
     * @param dcmHelper   dcmHelper
     * @param <T>         业务实体类
     * @param <DE>        Dcm实体类
     *
     * @return 被应用的项目
     */
    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> applyDcmConfig(List<RequestedConfigItem> diffs2Apply,
                                             IService<T> bizService,
                                             IDcmHelper<T, DE> dcmHelper
                                            ) {
        return applyDcmConfigInner(diffs2Apply,
                                   bizService::list,
                                   (bizEntities, dcmEntities) -> bizService.saveBatch(bizEntities) ? bizEntities.size() : 0,
                                   (bizEntities, dcmEntities) ->
                                           bizService.updateBatchById(bizEntities) ? bizEntities.size() : 0,
                                   (bizEntities, dcmEntities) -> bizService.removeByIds(bizEntities.stream()
                                                                                                .map(Entity::getId)
                                                                                                .collect(Collectors.toList()))
                                                                 ? bizEntities.size() : 0,
                                   dcmHelper);
    }

    /**
     * 应用变更项目
     * 这是使用ITreeDcmHelper 应用变更中的新增项目保存版本， 限制同前一版本。
     */
    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> treeApplyDcmConfig(List<RequestedConfigItem> diffs2Apply,
                                                 IService<T> bizService,
                                                 ITreeDcmHelper<T, DE> dcmHelper
                                                ) {
        return applyDcmConfigInner(diffs2Apply, bizService::list,
                                   (bizEntities, dcmEntities) -> {
                                       dcmHelper.treeSaveBatch(bizService, dcmEntities);
                                       return dcmEntities.size();
                                   },
                                   (bizEntities, dcmEntities) -> bizService.updateBatchById(bizEntities) ? bizEntities.size() : 0,
                                   (bizEntities, dcmEntities) -> bizService.removeByIds(bizEntities.stream()
                                                                                                .map(Entity::getId)
                                                                                                .collect(Collectors.toList()))
                                                                 ? bizEntities.size() : 0,
                                   dcmHelper);
    }

    /**
     * 应用变更项目, 本版本将更新/删除/新增，存在的业务实体获取均以function 接口形态加以提供。。
     */
    public static <T extends Entity<Long>, DE extends Identifiable>
    List<RequestedConfigItem> applyDcmConfig(List<RequestedConfigItem> diffs2Apply,
                                             Supplier<List<T>> existedBizEntitiesSupplier,
                                             BiFunction<Collection<T>, Collection<DE>, Integer> batchAdder,
                                             BiFunction<Collection<T>, Collection<DE>, Integer> batchUpdater,
                                             BiFunction<Collection<T>, Collection<DE>, Integer> batchRemover,
                                             IDcmHelper<T, DE> dcmHelper
                                            ) {
        return applyDcmConfigInner(diffs2Apply, existedBizEntitiesSupplier, batchAdder, batchUpdater, batchRemover, dcmHelper);
    }
}
