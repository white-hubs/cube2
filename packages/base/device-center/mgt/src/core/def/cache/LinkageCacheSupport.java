package com.guhecloud.cube.iot.mgt.core.def.cache;

import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.LinkageConfig;

public interface LinkageCacheSupport {
    String UPDATE_KEY = "UPDATE";
    String ADD_KEY = "ADD";
    String DELETE = "DELETE";

    /**
     * 基于数据库中的联动规则，解析并写入缓存
     *
     * @param linkConfigId 联动规则id
     * @return 联动触发元数据信息
     */
    LinkageConfig parseAndWriteCacheRule(Long linkConfigId);

    /**
     * 删除缓存中的联动信息
     *
     * @param linkConfigId 配置ID
     */
    void removeCacheRule(Long linkConfigId);

    /**
     * 发送事件通知规则变更
     */
    void publishLinkageChangeEvent(String eventType, Long linkageId);
}
