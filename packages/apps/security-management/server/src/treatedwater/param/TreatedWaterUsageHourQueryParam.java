package com.guhecloud.cube.srv.gen.securitymanagement.server.treatedwater.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 净化水使用量-小时表
 * @date 2023-10-30 15:08:36
 */
@Data
public class TreatedWaterUsageHourQueryParam extends Paging {
    //关键字
    private String keyword;
    //时间段
    private Period period;
}