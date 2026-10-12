package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;

/**
 * DCM 比较需要的Key 抽取器
 *
 * @param <BE>
 * @param <DE>
 */
public interface IDcmKeyExtractor<BE, DE extends Identifiable> {
    /**
     * 从Dcm实体中获取能唯一标识该实体的Key
     */
    String getDcmKey(DE dcmEntity);

    /**
     * 从业务实体中获取能唯一标识该实体的Key
     */
    String getBizKey(BE bizEntity);

}
