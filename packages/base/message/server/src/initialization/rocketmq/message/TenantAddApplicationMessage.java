package com.guhecloud.cube.base.message2.server.initialization.rocketmq.message;

import lombok.Data;

import java.util.List;

/**
 * 消息模板初始化消息
 *
 * @author lanchun
 * @date 2023/7/3 10:51
 */
@Data
public class TenantAddApplicationMessage {

    public static final String TOPIC = "TENANT_ADD_APPLICATION";

    /**
     * 应用编号列表
     */
    private List<String> applicationCodes;

    /**
     * 租户编号
     */
    private Integer tenant;
}
