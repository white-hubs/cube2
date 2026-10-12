package com.guhecloud.cube.srv.gen.energymanagement.server.vo;

import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.ChargeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeConfigParam;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.Builder;
import lombok.Data;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lrj
 * @Description 能源收费配置
 * @date 2022-07-04 15:12:36
 */
@Data
@Builder
public class EnergyChargeConfigExtendVO {
    List<EnergyChargeConfigParam> configs;

    public String getFPGValue(String instrumentType, LocalDateTime dateTime) {
        if (!CollectionUtils.isEmpty(configs)) {
            EnergyChargeConfigParam param = configs.stream().filter(d -> d.getInstrumentType().equals(instrumentType)).findFirst()
                    .orElse(null);
            if (param != null && param.getChargeType().equals(ChargeTypeEnum.FPG.name())) {
                return getFPGByTime(param.getPeriodPrices(), dateTime);
            }
        }
        return null;
    }

    //通过时间获取当前的价格
    private String getFPGByTime(List<EnergyChargeConfigParam.PeriodPrice> periodPrices, LocalDateTime dateTime) {
        if (!CollectionUtils.isEmpty(periodPrices)) {
            EnergyChargeConfigParam.PeriodPrice periodPrice = periodPrices.stream().filter(p -> {
                List<EnergyChargeConfigParam.PeriodDefine> periodDefines = p.getPeriodDefines();
                EnergyChargeConfigParam.PeriodDefine periodDefine = periodDefines.stream().filter(d -> {
                    String currentDate = DateUtils.formatDate(dateTime, "yyyy-MM-dd");
                    LocalDateTime startTime = DateUtils.parse(currentDate + " " + d.getStartTime());
                    LocalDateTime endTime = DateUtils.parse(currentDate + " " + d.getEndTime());
                    if (DateUtils.toTimestamp(dateTime) >= DateUtils.toTimestamp(startTime) &&
                        DateUtils.toTimestamp(dateTime) < DateUtils.toTimestamp(endTime)) {
                        return true;
                    } else {
                        return false;
                    }
                }).findAny().orElse(null);
                //是否找到匹配的时间段
                if (periodDefine != null) {
                    return true;
                } else {
                    return false;
                }
            }).findAny().orElse(null);
            return periodPrice != null ? periodPrice.getPeriodType() : null;
        }
        return null;
    }
}