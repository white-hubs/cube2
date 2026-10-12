package com.guhecloud.cube.base.iot.product.model.v3.event;

import lombok.Builder;
import lombok.Data;

/**
 * 版本回滚事件
 *
 * <p>当需要回滚到历史版本时发布此事件,触发数据恢复操作</p>
 *
 * @author system
 * @since 1.0
 */
@Data
@Builder
public class VersionRollbackEvent {

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 目标版本ID
     */
    private Long targetVersionId;

    /**
     * 版本快照数据(JSON格式)
     */
    private String snapshotData;
}
