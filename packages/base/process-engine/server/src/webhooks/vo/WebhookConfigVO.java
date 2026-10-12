package com.guhecloud.cube.base.flow.webhooks.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookConfigVO {
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
