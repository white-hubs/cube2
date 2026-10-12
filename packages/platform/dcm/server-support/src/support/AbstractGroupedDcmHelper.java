package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractGroupedDcmHelper<BE, DE extends Identifiable> extends AbstractSimpleDcmHelper<BE, DE> implements IDcmHelper<BE, DE> {

    public AbstractGroupedDcmHelper(Class<BE> bizClass, Class<DE> dcmClass) {
        super(bizClass, dcmClass);
    }

    @Override
    public Collection<? extends DE> convertRci2Dcm(RequestedConfigItem rci) {
        try {
            Collection<? extends DE> apps = super.convertRci2Dcm(rci);
            updateDcmGroupInfoWithRci(apps, rci);
            return apps;
        } catch (RuntimeException re) {
            log.warn("converRci2Dcm failed, rci [{}]will be ignored. error: {}", rci, re.toString());
            return Collections.emptyList();
        }
    }

    public DE convertBiz2Dcm(BE bizEntity) {
        try {
            DE dcmEntity = super.convertBiz2Dcm(bizEntity);
            updateDcmGroupInfoWithBiz(dcmEntity, bizEntity);
            return dcmEntity;
        } catch (RuntimeException re) {
            log.warn("convertBiz2Dcm failed and item will be ignored. bizEntity: {}, error: {}", bizEntity, re.toString());
            log.debug("convertBiz2Dcm failed and item will be ignored.", re);
            return null;
        }
    }

    public BE convertDcm2Biz(DE dcmEntity) {
        try {
            BE bizEntity = super.convertDcm2Biz(dcmEntity);
            updateBizGroupInfoWithDcm(bizEntity, dcmEntity);
            return bizEntity;
        } catch (RuntimeException re) {
            log.warn("convert {} to {} failed and will be ignored.: error: {}", dcmEntity, bizClass.getName(),
                     re.toString());
            return null;
        }
    }

    @Override
    public Collection<RequestedConfigItem> convertDcm2Rci(Collection<? extends DE> dcmEntities) {
        if (dcmEntities == null || dcmEntities.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Object, List<DE>> appMap = dcmEntities.stream()
                .collect(Collectors.groupingBy(this::generateGroupKey, LinkedHashMap::new, Collectors.toList()));
        return appMap.entrySet().stream()
                .map(x -> {
                    if (x.getValue().isEmpty()) {
                        return null;
                    } else {
                        return buildRciViaGroupKeyAndDcm(x.getKey(), x.getValue());
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 生成分组key对象
     *
     * @param dcmEntity 待生成分组信息的DCM实体
     *
     * @return 一个作为分组key的值对象
     */
    protected abstract Object generateGroupKey(DE dcmEntity);

    /**
     * 根据分组key和组内的Dcm实体， 构建RCI对象。 和 #generateGroupKey(DE) 方法配合使用
     *
     * @param groupKey    分组对象
     * @param dcmEntities 组内的Dcm实体. 不会为空
     *
     * @return 转换后的RCI对象
     */
    protected abstract RequestedConfigItem buildRciViaGroupKeyAndDcm(Object groupKey, Collection<? extends DE> dcmEntities);

    /**
     * 基于RCI更新DCM实体中的分组信息。<br>
     * <p>本方法应用在对请求的RCI和现存的配置间对比时将RCI转换为配置业务实体。<br/>
     * 调用时dcmEntities仅包含由RCI的vargs能承载的信息，一般情况下尚不包含kwargs中携带的分组信息。</p>
     *
     * @param dcmEntities 当前配置中存在的DCM实体， 这些实体中仅包含vargs中携带的信息。
     * @param rci         kwargs中携带了分组信息的RCI对象
     */
    protected abstract void updateDcmGroupInfoWithRci(Collection<? extends DE> dcmEntities, RequestedConfigItem rci);

    /**
     * 基于DCM实体更新一个新建业务实体。
     * <p>
     * 本方法用于对即将新建的业务记录中的字段进行更新，在调用前默认已经使用DCM实体中的同名字段
     * 对于传入的业务实体进行了更新， 但由于分组信息字段通常在业务实体中和在DCM实体中采用了不同名称，
     * 需要进行处理。另外非分组信息字段如果采用不同的字段名称与格式，通常也需要在这里进行处理。<br>
     * </p>
     *
     * @param bizEntity 待更新的业务实体
     * @param dcmEntity 用作更新信息源的配置实体
     */
    protected abstract void updateBizGroupInfoWithDcm(BE bizEntity, DE dcmEntity);

    /**
     * 基于业务实体更新DCM实体。
     * <p>
     * 本方法用于支持convertBiz2Dcm实现， 在调用本方法前，已经由业务实体中的同名字段复制到了配置实体中，
     * 但由于分组信息字段通常在业务实体中和在DCM实体中采用了不同名称，需要进行处理。另外非分组信息的字段，
     * 如果其字段名称与业务实体中的对应字段名称或定义不符，也需要进行处理。
     * </p>
     *
     * @param bizEntity 待更新的配置实体
     * @param dcmEntity 用作更新信息源的业务实体
     */
    protected abstract void updateDcmGroupInfoWithBiz(DE dcmEntity, BE bizEntity);

}
