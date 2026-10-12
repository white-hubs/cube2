package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lrj
 * @Description 能耗费用配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyChargeConfigParam {
    //仪表类型
    private String instrumentType;
    private String chargeType;//收费类型
    private Boolean status;

    private List<PeriodPrice> periodPrices;
    private List<PeriodDefine> periodDefines;
    private List<UsagePrice> usagePrices;

    @Data
    public static class PeriodPrice {
        private String periodType;
        private BigDecimal price;
        private List<PeriodDefine> periodDefines;
    }

    @Data
    public static class PeriodDefine {
        private String periodType;
        private String startTime;
        private String endTime;
    }

    @Data
    public static class UsagePrice {
        private BigDecimal startUsage;
        private BigDecimal endUsage;
        private BigDecimal price;
    }

}