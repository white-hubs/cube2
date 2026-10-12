package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.Identifiable;

/**
 * 用于diff/apply 的过滤处理通用逻辑归集.
 *
 * @param <BE> 业务实体类型
 * @param <DE> dcm 用的内部表示实体
 */
public interface IDcmHelper<BE, DE extends Identifiable> extends
        IDcmConverter<BE, DE>, IDcmKeyExtractor<BE, DE> {

}
