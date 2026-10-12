package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyGate;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.AlarmRuleNameEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.MeterReadingCycleEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyGateService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AlarmRuleNameEnumVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.InstrumentTypeEnumVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadingCycleEnumVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 枚举值获取
 * @author: lw
 * @date: 2022/7/5
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnumTypeController {

    private final EnergyGateService energyGateService;

    /**
     * 获取仪表枚举中所有的配置信息
     */
    @GetMapping(value = "/enumType/getInstumenttype")
    public List<InstrumentTypeEnumVO> getInstumenttype() {
        List<InstrumentTypeEnumVO> enumTypeVo = InstrumentTypeEnum.getEnumTypeVo();
        List<EnergyGate> list = energyGateService.list(Wrappers.<EnergyGate>lambdaQuery()
                                                               .eq(EnergyGate::getOpen, true));
        List<String> collect = list.stream().map(EnergyGate::getEnergyType).collect(Collectors.toList());
        List<InstrumentTypeEnumVO> objects = new ArrayList<>();
        if (collect.size() > 0) {
            for (InstrumentTypeEnumVO instrumentTypeEnumVO : enumTypeVo) {
                if (collect.contains(instrumentTypeEnumVO.getCode())) {
                    objects.add(instrumentTypeEnumVO);
                }
            }

        }

        return objects;
    }

    /**
     * 获取告警事件枚举中所有的配置信息
     */
    @GetMapping(value = "/enumType/getalarmRuleName")
    public List<AlarmRuleNameEnumVO> getAlarmRuleName() {
        return AlarmRuleNameEnum.getEnumTypeVo();
    }

    /**
     * 获取抄表周期枚举中所有的配置信息
     */
    @GetMapping(value = "/enumType/getReadingCycleRuleName")
    public List<MeterReadingCycleEnumVO> getReadingCycleRuleName() {
        return MeterReadingCycleEnum.getEnumTypeVo();
    }

}
