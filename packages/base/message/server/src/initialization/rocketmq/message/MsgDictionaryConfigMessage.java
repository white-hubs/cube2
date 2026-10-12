package com.guhecloud.cube.base.message2.server.initialization.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典消息发送配置
 *
 * @author lanchun
 * @date 2025/1/4 16:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgDictionaryConfigMessage {
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
    /**
     * 操作类型
     */
    private String operationType;
}
