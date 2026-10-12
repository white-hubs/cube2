package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureAlertGlobalConfigMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertGlobalConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertGlobalConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertGlobalConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** 粮仓温度趋势预警全局默认配置。 */
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureAlertGlobalConfigServiceImpl
        extends ServiceImpl<WarehouseTemperatureAlertGlobalConfigMapper, WarehouseTemperatureAlertGlobalConfig>
        implements WarehouseTemperatureAlertGlobalConfigService {

    private static final int SCALE = 2;
    private static final BigDecimal DEFAULT_MIN_DAILY_INCREASE = new BigDecimal("0.30");
    private static final BigDecimal DEFAULT_WARNING_THRESHOLD = new BigDecimal("25.00");
    private static final BigDecimal DEFAULT_APPROACH_MARGIN = new BigDecimal("3.00");

    @Override
    public WarehouseTemperatureAlertGlobalConfigVO detail() {
        WarehouseTemperatureAlertGlobalConfig entity = list().stream().findFirst().orElse(null);
        return toVO(entity);
    }

    @Override
    public Boolean saveConfig(WarehouseTemperatureAlertGlobalConfigSaveParam param) {
        WarehouseTemperatureAlertGlobalConfig entity = list().stream().findFirst().orElse(null);
        if (entity == null) {
            entity = new WarehouseTemperatureAlertGlobalConfig();
            entity.setId(IdWorker.getId());
            save(entity);
        }
        entity.setConsecutiveDays(normalizeInteger(param == null ? null : param.getConsecutiveDays(), 3));
        entity.setMinDailyIncrease(normalizeDecimal(param == null ? null : param.getMinDailyIncrease(), DEFAULT_MIN_DAILY_INCREASE));
        entity.setWarningThreshold(normalizeDecimal(param == null ? null : param.getWarningThreshold(), DEFAULT_WARNING_THRESHOLD));
        entity.setApproachMargin(normalizeDecimal(param == null ? null : param.getApproachMargin(), DEFAULT_APPROACH_MARGIN));
        updateById(entity);
        return true;
    }

    @Override
    public WarehouseTemperatureAlertGlobalConfigVO resolveEffectiveConfig() {
        return detail();
    }

    private WarehouseTemperatureAlertGlobalConfigVO toVO(WarehouseTemperatureAlertGlobalConfig entity) {
        WarehouseTemperatureAlertGlobalConfigVO vo = new WarehouseTemperatureAlertGlobalConfigVO();
        vo.setId(entity == null ? null : entity.getId());
        vo.setConsecutiveDays(entity == null ? null : entity.getConsecutiveDays());
        vo.setMinDailyIncrease(entity == null ? null : scale(entity.getMinDailyIncrease()));
        vo.setWarningThreshold(entity == null ? null : scale(entity.getWarningThreshold()));
        vo.setApproachMargin(entity == null ? null : scale(entity.getApproachMargin()));
        return vo;
    }

    private static Integer normalizeInteger(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    private static BigDecimal normalizeDecimal(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : scale(value);
    }

    private static BigDecimal scale(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
