package com.guhecloud.cube.biz.meattrade.server.base.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础配置信息
 */
@Data
public class ConfigVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务类型
     */
    private ConfigEnum type;

    /**
     * 业务数值
     */
    @Decimal
    private BigDecimal value;

}
