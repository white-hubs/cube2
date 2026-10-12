package com.guhecloud.cube.base.message2.client.dto;

import lombok.Data;

import java.util.List;

/**
 * 平台消息配置返回参数
 *
 * @author lanchun
 * @date 2021/11/10 17:18
 */
@Data
public class SatMsgConfigDTO {

    /**
     * 消息配置主键
     */
    private Long id;

    /**
     * 消息定义主键
     */
    private Long satMsgDefineId;

    /**
     * 通知方表达式
     */
    private String notifyExpression;

    /**
     * 消息编号
     */
    private String satMsgDefineCode;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 模板主键
     */
    private String msgTmpId;

    /**
     * 模板内容
     */
    private String msgTmpContent;

    /**
     * 消息通道配置
     */
    private List<SatMsgChannelConfigDTO> satMsgChannelConfigDTOS;
}
