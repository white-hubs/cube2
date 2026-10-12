package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class MarketMonthlyReportUrlQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 开关状态
     */
    private Boolean enabled;
}
