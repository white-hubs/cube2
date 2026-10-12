package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyChargeConfigMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyChargeConfig;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.ChargeTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyChargeConfigParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyChargeConfigService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigExtendVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyChargeConfigVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：能耗收费配置服务
 *
 * @author appet
 * @date 2023-03-30 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyChargeConfigServiceImpl extends ServiceImpl<EnergyChargeConfigMapper, EnergyChargeConfig> implements EnergyChargeConfigService {

    @Override
    public void add(EnergyChargeConfigParam param) {
        //检查时间段是否重复
        Assert.isConflict(checkPeriodOverlap(param.getPeriodDefines()), "时间段重叠，请更正后再试");
        //先删除之前的配置
        this.baseMapper.delete(Wrappers.<EnergyChargeConfig>lambdaQuery()
                                       .eq(EnergyChargeConfig::getInstrumentType, param.getInstrumentType()));
        //构建数据
        if (CollectionUtils.isNotEmpty(param.getPeriodPrices())
            && CollectionUtils.isNotEmpty(param.getPeriodDefines())) {
            List<EnergyChargeConfigParam.PeriodPrice> periodPrices = param.getPeriodPrices();
            List<EnergyChargeConfigParam.PeriodDefine> periodDefines = param.getPeriodDefines();
            //时段定义分组
            Map<String, List<EnergyChargeConfigParam.PeriodDefine>> groupMap = periodDefines.stream()
                    .collect(Collectors.groupingBy(EnergyChargeConfigParam.PeriodDefine::getPeriodType));
            periodPrices.stream().forEach(d -> {
                d.setPeriodDefines(groupMap.get(d.getPeriodType()));
            });
            //保存数据
            EnergyChargeConfig build = EnergyChargeConfig.builder()
                    .instrumentType(param.getInstrumentType())
                    .chargeType(ChargeTypeEnum.FPG.name())
                    .status(ChargeTypeEnum.FPG.name().equals(param.getChargeType()))
                    .jsonConfig(JsonUtils.serialize(periodPrices))
                    .build();
            this.baseMapper.insert(build);
        }
        if (CollectionUtils.isNotEmpty(param.getUsagePrices())) {
            EnergyChargeConfig build = EnergyChargeConfig.builder()
                    .instrumentType(param.getInstrumentType())
                    .chargeType(ChargeTypeEnum.JTJ.name())
                    .status(ChargeTypeEnum.JTJ.name().equals(param.getChargeType()))
                    .jsonConfig(JsonUtils.serialize(param.getUsagePrices()))
                    .build();
            this.baseMapper.insert(build);
        }
    }

    //检查时间段是否重叠
    private Boolean checkPeriodOverlap(List<EnergyChargeConfigParam.PeriodDefine> periodDefines) {
        for (int i = 0; periodDefines != null && i < periodDefines.size(); i++) {
            EnergyChargeConfigParam.PeriodDefine timePeriod1 = periodDefines.get(i);
            long startTime1 = DateUtils.toTimestamp(DateUtils.parse(timePeriod1.getStartTime()));
            long endTime1 = DateUtils.toTimestamp(DateUtils.parse(timePeriod1.getEndTime()));
            for (int j = i + 1; j < periodDefines.size(); j++) {
                EnergyChargeConfigParam.PeriodDefine timePeriod2 = periodDefines.get(j);
                long startTime2 = DateUtils.toTimestamp(DateUtils.parse(timePeriod2.getStartTime()));
                long endTime2 = DateUtils.toTimestamp(DateUtils.parse(timePeriod2.getEndTime()));
                if (!(endTime1 <= startTime2 || endTime2 <= startTime1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EnergyChargeConfigVO getByType(String instrumentType, String chargeType) {
        List<EnergyChargeConfig> energyChargeConfigs = this.baseMapper.selectList(Wrappers.<EnergyChargeConfig>lambdaQuery()
                                                                                          .eq(EnergyChargeConfig::getInstrumentType, instrumentType)
                                                                                          .eq(StringUtils.isNotBlank(chargeType), EnergyChargeConfig::getChargeType, chargeType));
        EnergyChargeConfigVO build = new EnergyChargeConfigVO();
        if (CollectionUtils.isNotEmpty(energyChargeConfigs)) {
            build.setInstrumentType(instrumentType);
            energyChargeConfigs.stream().forEach(config -> {
                String jsonConfig = config.getJsonConfig();
                if (ChargeTypeEnum.FPG.name().equals(config.getChargeType())) {
                    List<EnergyChargeConfigParam.PeriodPrice> periodPrices = JsonUtils.deserialize(jsonConfig, new TypeReference<List<EnergyChargeConfigParam.PeriodPrice>>() {});
                    build.setPeriodPrices(periodPrices);
                    List<EnergyChargeConfigParam.PeriodDefine> periodDefines = periodPrices.stream()
                            .flatMap(d -> d.getPeriodDefines().stream()).collect(Collectors.toList());
                    build.setPeriodDefines(periodDefines);
                    if (config.getStatus()) {
                        build.setChargeType(ChargeTypeEnum.FPG.name());
                    }
                }
                if (ChargeTypeEnum.JTJ.name().equals(config.getChargeType())) {
                    List<EnergyChargeConfigParam.UsagePrice> usagePrice = JsonUtils.deserialize(jsonConfig, new TypeReference<List<EnergyChargeConfigParam.UsagePrice>>() {});
                    build.setUsagePrices(usagePrice);
                    if (config.getStatus()) {
                        build.setChargeType(ChargeTypeEnum.JTJ.name());
                    }
                }
            });
        }
        return build;
    }

    @Override
    public EnergyChargeConfigExtendVO getValidConfig() {
        List<EnergyChargeConfig> energyChargeConfigs = this.baseMapper.selectList(Wrappers.<EnergyChargeConfig>lambdaQuery()
                                                                                          .eq(EnergyChargeConfig::getStatus, true));
        if (CollectionUtils.isNotEmpty(energyChargeConfigs)) {
            List<EnergyChargeConfigParam> collect = energyChargeConfigs.stream().map(config -> {
                EnergyChargeConfigParam build = new EnergyChargeConfigParam();
                build.setInstrumentType(config.getInstrumentType());
                build.setChargeType(config.getChargeType());
                String jsonConfig = config.getJsonConfig();
                if (ChargeTypeEnum.FPG.name().equals(config.getChargeType())) {
                    List<EnergyChargeConfigParam.PeriodPrice> periodPrices = JsonUtils.deserialize(jsonConfig, new TypeReference<List<EnergyChargeConfigParam.PeriodPrice>>() {
                    });
                    build.setPeriodPrices(periodPrices);
                    List<EnergyChargeConfigParam.PeriodDefine> periodDefines = periodPrices.stream()
                            .flatMap(d -> d.getPeriodDefines().stream()).collect(Collectors.toList());
                    build.setPeriodDefines(periodDefines);
                }
                if (ChargeTypeEnum.JTJ.name().equals(config.getChargeType())) {
                    List<EnergyChargeConfigParam.UsagePrice> usagePrice = JsonUtils.deserialize(jsonConfig, new TypeReference<List<EnergyChargeConfigParam.UsagePrice>>() {
                    });
                    build.setUsagePrices(usagePrice);
                }
                return build;
            }).collect(Collectors.toList());
            return EnergyChargeConfigExtendVO.builder().configs(collect).build();
        }
        return null;
    }
}