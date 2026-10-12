package com.guhecloud.cube.base.message2.server.base.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息应用分组统计
 *
 * @author lanchun
 * @date 2021/12/13 10:39
 */
@Data
public class MsgRecordApplicationTotalVO {

    /**
     * 数量
     */
    private Integer total;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 最新时间
     */
    private LocalDateTime lastTime;
}
