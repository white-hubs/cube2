package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.Collection;

/**
 * DCM 需要的实体转换逻辑接口
 *
 * @param <BE>
 * @param <DE>
 */
public interface IDcmConverter<BE, DE extends Identifiable> extends IDcmRciConverter<DE> {
    /**
     * 是否TreeStyle 结构
     */
    boolean isTreeStyle();

    /**
     * Biz -> Dcm 转换
     */
    DE convertBiz2Dcm(BE bizEntity);

    /**
     * Dcm -> Biz 转换, 用于新增记录时构建
     */
    BE convertDcm2Biz(DE dcmEntity);

    /**
     * 用Dcm 去更新一个已经存在的实体类， 用于更新记录构建
     */
    void updateBizWithDcm(BE input, DE dcmEntity);

    /**
     * 以下方法仅提供作为操作界面， 有默认实现， 不需要基础实现
     */
    Collection<RequestedConfigItem> convertBiz2Rci(Collection<? extends BE> bizEntities);
}
