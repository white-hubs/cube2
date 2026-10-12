package com.guhecloud.cube.base.bracing.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Set;

/**
 * @author wql
 * @describe 消息发送参数
 * @date 2023/9/4 15:50
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendParam {
    /**
     * 消息配置id
     */
    private Long notifyId;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 应用code
     */
    private String applicationCode;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 提醒人员
     */
    private Set<Long> notifyUsers;
    /**
     * 主要业务
     */
    private Object businessPrincipal;

    /**
     * 场景编码
     */
    private String msgSceneCode;

    /**
     * 消息参数
     */
    private Map<String, String> tempParams;
}
