package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SafetyEducationThemeRatioVO {

    private String id;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 任务数量
     */
    private Integer totalNum;

    /**
     * 主题占比
     */
    @Decimal
    private BigDecimal ratio;
}
