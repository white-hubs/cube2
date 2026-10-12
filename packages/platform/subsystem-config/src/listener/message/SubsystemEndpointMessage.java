package com.guhecloud.subsystem.config.listener.message;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/18 16:59
 * @Description:
 */
@Data
public class SubsystemEndpointMessage {
    public static final String TOPIC = "DICTIONARY";

    /**
     * 字典项
     */
    private String itemCode;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 租户code
     */
    private String tenant;
    /**
     * 字典变量
     */
    private String variable;
    /**
     * 字典状态
     */
    private String status;
    /**
     * 字典类型
     */
    private String type;
}
