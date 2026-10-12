package com.guhecloud.cube.base.flow.webhooks.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 业务回调配置表
 *
 * @TableName webhook_config
 */
@Data
@TableName("webhook_config")
public class WebhookConfig extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 流程表单id
     */
    private String fromFlowId;

    /**
     * 回调节点
     */
    private String triggerNode;

    /**
     * 回调地址
     */
    private String url;

    /**
     * 密钥
     */
    private String token;

}