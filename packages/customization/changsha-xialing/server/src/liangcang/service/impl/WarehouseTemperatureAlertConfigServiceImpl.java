package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.client.WarehousePageApiClient;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehouseTemperatureAlertConfigMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureAlertConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureAlertGlobalConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertWarehouseConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

/** 粮仓温度趋势预警单仓配置。 */
@Service
@RequiredArgsConstructor
public class WarehouseTemperatureAlertConfigServiceImpl
        extends ServiceImpl<WarehouseTemperatureAlertConfigMapper, WarehouseTemperatureAlertConfig>
        implements WarehouseTemperatureAlertConfigService {

    private static final int SCALE = 2;

    private final WarehouseTemperatureAlertGlobalConfigService globalConfigService;
    private final WarehousePageApiClient warehousePageApiClient;

    @Override
    public WarehouseTemperatureAlertWarehouseConfigVO detail(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        WarehouseTemperatureAlertConfig entity = lambdaQuery()
                .eq(WarehouseTemperatureAlertConfig::getWarehouseId, warehouseId)
                .one();
        WarehouseTemperatureAlertGlobalConfigVO globalConfig = globalConfigService.resolveEffectiveConfig();
        WarehouseTemperatureAlertGlobalConfigVO effectiveConfig = resolveEffectiveConfig(warehouseId);
        Map<Long, LiangcangWarehousePageItemVO> warehouseMap = warehousePageApiClient.fetchPageItemsByIds(Set.of(warehouseId));
        LiangcangWarehousePageItemVO warehouse = warehouseMap.get(warehouseId);

        WarehouseTemperatureAlertWarehouseConfigVO vo = new WarehouseTemperatureAlertWarehouseConfigVO();
        vo.setWarehouseId(warehouseId);
        vo.setWarehouseName(warehouse == null ? null : warehouse.getName());
        vo.setGrainType(warehouse == null ? null : warehouse.getCategoryName());
        vo.setUseGlobalDefault(entity == null ? null : entity.getUseGlobalDefault());
        vo.setPopupEnabled(entity == null ? null : entity.getPopupEnabled());
        vo.setGlobalConfig(globalConfig);
        vo.setWarehouseConfig(toVO(entity));
        vo.setEffectiveConfig(effectiveConfig);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveConfig(WarehouseTemperatureAlertWarehouseConfigSaveParam param) {
        Assert.isParameterError(param == null, MsgBinding.TEMP_ALERT_WAREHOUSE_CONFIG_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        WarehouseTemperatureAlertConfig entity = lambdaQuery()
                .eq(WarehouseTemperatureAlertConfig::getWarehouseId, param.getWarehouseId())
                .one();
        if (entity == null) {
            entity = new WarehouseTemperatureAlertConfig();
            entity.setId(IdWorker.getId());
            entity.setWarehouseId(param.getWarehouseId());
            save(entity);
        }
        fill(entity, param);
        updateById(entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetToGlobalDefault(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        WarehouseTemperatureAlertConfig entity = lambdaQuery()
                .eq(WarehouseTemperatureAlertConfig::getWarehouseId, warehouseId)
                .one();
        if (entity == null) {
            entity = new WarehouseTemperatureAlertConfig();
            entity.setId(IdWorker.getId());
            entity.setWarehouseId(warehouseId);
            save(entity);
        }
        entity.setUseGlobalDefault(Boolean.TRUE);
        entity.setConsecutiveDays(null);
        entity.setMinDailyIncrease(null);
        entity.setWarningThreshold(null);
        entity.setApproachMargin(null);
        entity.setPopupEnabled(Boolean.TRUE);
        updateById(entity);
        return true;
    }

    @Override
    public WarehouseTemperatureAlertGlobalConfigVO resolveEffectiveConfig(Long warehouseId) {
        WarehouseTemperatureAlertConfig entity = lambdaQuery()
                .eq(WarehouseTemperatureAlertConfig::getWarehouseId, warehouseId)
                .one();
        WarehouseTemperatureAlertGlobalConfigVO globalConfig = globalConfigService.resolveEffectiveConfig();
        if (entity == null || Boolean.TRUE.equals(entity.getUseGlobalDefault())) {
            return globalConfig;
        }
        WarehouseTemperatureAlertGlobalConfigVO vo = new WarehouseTemperatureAlertGlobalConfigVO();
        vo.setId(globalConfig.getId());
        vo.setConsecutiveDays(entity.getConsecutiveDays() == null ? globalConfig.getConsecutiveDays() : entity.getConsecutiveDays());
        vo.setMinDailyIncrease(entity.getMinDailyIncrease() == null ? globalConfig.getMinDailyIncrease() : scale(entity.getMinDailyIncrease()));
        vo.setWarningThreshold(entity.getWarningThreshold() == null ? globalConfig.getWarningThreshold() : scale(entity.getWarningThreshold()));
        vo.setApproachMargin(entity.getApproachMargin() == null ? globalConfig.getApproachMargin() : scale(entity.getApproachMargin()));
        return vo;
    }

    private void fill(WarehouseTemperatureAlertConfig entity, WarehouseTemperatureAlertWarehouseConfigSaveParam param) {
        entity.setUseGlobalDefault(Boolean.TRUE.equals(param.getUseGlobalDefault()));
        entity.setConsecutiveDays(param.getConsecutiveDays() == null ? null : param.getConsecutiveDays());
        entity.setMinDailyIncrease(scale(param.getMinDailyIncrease()));
        entity.setWarningThreshold(scale(param.getWarningThreshold()));
        entity.setApproachMargin(scale(param.getApproachMargin()));
        entity.setPopupEnabled(param.getPopupEnabled() == null ? Boolean.TRUE : param.getPopupEnabled());
    }

    private WarehouseTemperatureAlertConfigVO toVO(WarehouseTemperatureAlertConfig entity) {
        if (entity == null) {
            return null;
        }
        WarehouseTemperatureAlertConfigVO vo = new WarehouseTemperatureAlertConfigVO();
        vo.setId(entity.getId());
        vo.setWarehouseId(entity.getWarehouseId());
        vo.setUseGlobalDefault(entity.getUseGlobalDefault());
        vo.setConsecutiveDays(entity.getConsecutiveDays());
        vo.setMinDailyIncrease(entity.getMinDailyIncrease());
        vo.setWarningThreshold(entity.getWarningThreshold());
        vo.setApproachMargin(entity.getApproachMargin());
        vo.setPopupEnabled(entity.getPopupEnabled());
        return vo;
    }

    private static BigDecimal scale(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
