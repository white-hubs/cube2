package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.DateEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 数据看板查询参数
 *
 * @author lzy
 * @date 2021/9/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StatQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 200, message = "关键字不能大于200")
    private String keyword;

    /**
     * 时间类别
     */
    private DateEnum type;

    /**
     * 作弊状态
     */
    private String cheatStatus;

    /**
     * 审核状态
     */
    private PorkArrangeAuditEnum auditStatus;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 时间
     */
    private Period period;

    /**
     * 来货报备主键集合
     */
    private Collection<Long> porkReportIds;

    /**
     * 市场租户编号
     */
    private Integer tenant;

    /**
     * 最低价
     */
    private BigDecimal lowerPrice;
}
