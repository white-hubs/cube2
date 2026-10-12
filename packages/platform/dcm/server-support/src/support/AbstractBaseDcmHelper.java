package com.guhecloud.platform.dcm.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractBaseDcmHelper<BE, DE extends Identifiable> implements IDcmHelper<BE, DE> {

    public static final String DEFAULT_VERSION = "1.0";

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected final Class<BE> bizClass;
    protected final Class<DE> dcmClass;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    AbstractBaseDcmHelper(Class<BE> bizClass, Class<DE> dcmClass) {
        this.bizClass = bizClass;
        this.dcmClass = dcmClass;
    }

    /**
     * 是否是tenanted
     */
    protected boolean isTenanted() {
        return BasicTenantEntity.class.isAssignableFrom(bizClass);
    }

    /**
     * 获取tenant 信息
     */
    protected String getTenantInfo(BE entity) {
        if (isTenanted()) {
            return String.valueOf(((BasicTenantEntity) entity).getTenant());
        } else {
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }
    }

    public boolean isTreeStyle() {
        return false;
    }

    @Override
    public String getDcmKey(DE dcmEntity) {
        return dcmEntity.getId();
    }

    @Override
    public DE convertBiz2Dcm(BE bizEntity) {
        try {
            return ConvertUtils.convert(dcmClass, bizEntity);
        } catch (RuntimeException re) {
            log.warn("convert biz to dcm failed: {}", re.toString());
            return null;
        }
    }

    public Collection<RequestedConfigItem> convertBiz2Rci(Collection<? extends BE> bizEntities) {
        return convertBiz2Rci(bizEntities, getDcmComparator());
    }

    protected Comparator<DE> getDcmComparator() {
        return null;
    }

    public Collection<RequestedConfigItem> convertBiz2Rci(Collection<? extends BE> bizEntities, Comparator<DE> dcmComparator) {
        if (bizEntities.isEmpty()) {
            return Collections.emptyList();
        }
        Stream<DE> dcmStream = bizEntities.stream()
                .map(this::convertBiz2DcmInner)
                .filter(Objects::nonNull); // 过滤出错的项目

        if (dcmComparator != null) {
            dcmStream = dcmStream.sorted(dcmComparator);
        }
        List<DE> dcmEntities = dcmStream
                .collect(Collectors.toList());
        Collection<RequestedConfigItem> rcis = convertDcm2Rci(dcmEntities);
        if (isTenanted()) {
            List<String> tenants = Collections.singletonList(
                    getTenantInfo(bizEntities.stream().findAny().get()));
            for (RequestedConfigItem rci : rcis) {
                rci.setTenants(tenants);
                rci.setIsSystem(Boolean.FALSE);
            }
        }
        return rcis;
    }

    private DE convertBiz2DcmInner(BE bizEntity) {
        try {
            return convertBiz2Dcm(bizEntity);
        } catch (RuntimeException re) {
            log.warn("convertBiz2Dcm failed, entry: {}, error: {}", bizEntity, re.toString());
            return null;
        }
    }

    @Override
    public BE convertDcm2Biz(DE dcmEntity) {
        try {
            return ConvertUtils.convert(bizClass, dcmEntity);
        } catch (RuntimeException re) {
            log.warn("convert dcm to biz failed: {}", re.toString());
            return null;
        }
    }

    /**
     * 从业务实体中获取能唯一标识该实体的Key,
     * 由于默认的getDcmKey实现为Identifiable的getId方法, 注意匹配.
     *
     * @see #getDcmKey(Identifiable)
     * @see Identifiable#getId()
     */
    @Override
    public abstract String getBizKey(BE bizEntity);

    /**
     * 使用Dcm实体中的信息更新业务实体。
     * 用于更新已经存在的实体，故其与配置唯一标识相关的字段可以不用考虑，
     * 只需要更新涉及fingerPrint的各个属性
     */
    @Override
    public abstract void updateBizWithDcm(BE input, DE dcmEntity);

    /**
     * 从Map对象解析实体， 有特殊逻辑者需要覆盖
     */
    protected DE dcmFromMap(Map<String, Object> object) {
        try {
            return ConvertUtils.convert(dcmClass, object);
        } catch (RuntimeException re) {
            log.debug("convert dcm from map failed: {}", re.toString());
            return null;
        }
    }

    /**
     * 从字符串解析Dcm实体， 如果需要支持更简化的规式， 需要override 本方法
     */
    protected DE dcmFromString(String object) {
        try {
            return objectMapper.readValue(object, dcmClass);
        } catch (JsonProcessingException e) {
            log.debug("parse content as {} failed: {}", dcmClass.getSimpleName(), e.getMessage());
            return null;
        }
    }

    protected RequestedConfigItem getRciTemplate() {
        RequestedConfigItem ret = new RequestedConfigItem();
        ret.setConfigType(getConfigType());
        ret.setIsSystem(Boolean.TRUE);
        ret.setVersion(DEFAULT_VERSION);
        ret.setIsRemoval(Boolean.FALSE);
        return ret;
    }

    /**
     * @return 返回支持的配置类型标识
     */
    protected abstract String getConfigType();
}
