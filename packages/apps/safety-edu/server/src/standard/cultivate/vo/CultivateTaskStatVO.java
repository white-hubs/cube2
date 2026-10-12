package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Appet
 * @Description 培训任务统计
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskStatVO {
    private BigDecimal cultivateNum;
    private BigDecimal themeNum;
    private BigDecimal examNum;
    private BigDecimal onlineNum;
    private BigDecimal offlineNum;
    private BigDecimal staffNum;
    private BigDecimal merchantNum;
    private BigDecimal qoq;
}