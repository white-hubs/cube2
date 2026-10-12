package com.guhecloud.cube.biz.meattrade.server.order.vo;

import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import lombok.Data;

import java.util.List;

/**
 * 结算配置信息
 */
@Data
public class SettlementConfigPageVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 计算名称
     */
    private String name;

    /**
     * 计算方式
     */
    private SettlementConfigEnum way;

    /**
     * 配置规则
     */
    private List<SettlementConfigVO> rules;

    /**
     * 是否选中
     */
    private Boolean flag;
}
