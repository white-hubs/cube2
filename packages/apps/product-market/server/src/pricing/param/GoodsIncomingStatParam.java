package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 来货量
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsIncomingStatParam extends Paging {
    //品类ID
    private List<Long> goodsCategoryIds;
    //查询时间范围
    //    @NotNull(message = "查询的时间范围不能为空")
    private Period period;
    //用于统计时 确认统计的方式：天(%Y-%m-%d) 月(%Y-%m) 年(%Y)
    private String groupDateFormat;
    //统计维度
    private String statType;
}