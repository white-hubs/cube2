package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.FPGTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeConfigParam;
import lombok.Data;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lrj
 * @Description 能源收费配置
 * @date 2022-07-04 15:12:36
 */
@Data
public class EnergyChargeConfigVO extends EnergyChargeConfigParam {

    public BigDecimal getPrice(FPGTypeEnum type) {
        List<PeriodPrice> periodPrices = super.getPeriodPrices();
        if (!CollectionUtils.isEmpty(periodPrices)) {
            PeriodPrice periodPrice = periodPrices.stream()
                    .filter(p -> p.getPeriodType().equals(type.name()))
                    .findFirst().orElse(null);
            return periodPrice != null ? periodPrice.getPrice() : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getFPGAmount(FPGTypeEnum type, BigDecimal value) {
        BigDecimal price = this.getPrice(type);
        return price.multiply(value).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    //通过用量获取当前价格
    public BigDecimal getAmoutByUsage(BigDecimal usage) {
        if (usage == null) {
            return BigDecimal.ZERO;
        }
        List<UsagePrice> usagePrices = super.getUsagePrices();
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(usagePrices)) {
            for (UsagePrice d : usagePrices) {
                //处理无穷大上限问题
                BigDecimal endUsage = d.getEndUsage();
                endUsage = endUsage != null ? endUsage : BigDecimal.valueOf(999999999);
                if (usage.compareTo(d.getStartUsage()) >= 0 && usage.compareTo(endUsage) <= 0) {
                    BigDecimal amount = usage.subtract(d.getStartUsage()).multiply(d.getPrice());
                    totalAmount = totalAmount.add(amount);
                } else if (usage.compareTo(d.getEndUsage()) > 0) {
                    BigDecimal amount = d.getEndUsage().multiply(d.getPrice());
                    totalAmount = totalAmount.add(amount);
                }
            }
        }
        return totalAmount;
    }
}