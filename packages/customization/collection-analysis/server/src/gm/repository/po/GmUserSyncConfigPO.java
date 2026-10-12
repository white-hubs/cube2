package com.guhecloud.cube.biz.collection.analysis.gm.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 光明人员同步配置
 */
@Data
@TableName("gm_user_sync_config")
@EqualsAndHashCode(callSuper = true)
public class GmUserSyncConfigPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 三方网关根地址，例如 http://www.guangming.com:46861/gateway
     */
    private String baseUrl;
    /**
     * 三方接口 appId
     */
    private String appId;
    /**
     * 三方接口 appKey
     */
    private String appKey;
    /**
     * 三方分页大小
     */
    private Integer pageSize;
    /**
     * 是否启用同步
     */
    private Boolean enabled;
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
}
