package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SortEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 销售统计查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StatisticsQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 排序字段
     */
    @Size(max = 100)
    private String item;

    /**
     * 升降序
     */
    private SortEnum sort;

    /**
     * 商户主键
     */
    private Long supplierId;

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
     * 商户主键集合
     */
    private Collection<Long> supplierIds;
}
