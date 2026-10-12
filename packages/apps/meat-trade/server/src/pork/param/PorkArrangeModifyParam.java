package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2021/12/6
 */
@Data
public class PorkArrangeModifyParam {

    /**
     * 报备主键
     */
    private Long porkReportId;

    /**
     * 理货片数
     */
    private Integer slices;

    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal weight;

    /**
     * 理货地点
     */
    private String place;

    /**
     * 理货状态
     */
    private PorkArrangeEnum status;

    /**
     * 理货完成时间
     */
    private LocalDateTime porkArrangeTime;

}
