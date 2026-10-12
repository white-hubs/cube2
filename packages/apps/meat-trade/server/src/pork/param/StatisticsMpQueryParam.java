package com.guhecloud.cube.biz.meattrade.server.pork.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.DateEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 小程序统计参数
 *
 * @author lzy
 * @date 2021/9/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StatisticsMpQueryParam extends Paging {

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 时间类别
     */
    private DateEnum type;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    /**
     * 时间
     */
    private Period period;
}
