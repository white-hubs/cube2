package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author appet
 * @Description 能耗统计
 */
@Data
public class EnergyDailyUsageStatParam {

    private String instrumentType;//仪表类型
    private Period period;
}