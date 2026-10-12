package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.impl;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.IllegalParkingBlacklistCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.IllegalParkingCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingJobService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalTimeCycleEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingBlacklistRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingConfigRepository;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/20 15:27
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingJobServiceImpl implements IllegalParkingJobService {
    private final IllegalParkingBlacklistCommandService illegalParkingBlacklistCommandService;
    private final IllegalParkingBlacklistRepository illegalParkingBlacklistRepository;
    private final IllegalParkingConfigRepository illegalParkingConfigRepository;
    private final IllegalParkingCommandService illegalParkingCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanIllegalParkingBlackList() {
        //获取违停配置
        IllegalParkingConfigPO parkingConfigPO = illegalParkingConfigRepository.lambdaQuery().
                last("limit 1").one();
        if (Objects.isNull(parkingConfigPO)) {
            log.error("未获取到违停配置");
            return;
        }
        Period timeCycle = IllegalTimeCycleEnum.getIllegalTimeCycle(parkingConfigPO.getTimeCycle());

        List<IllegalParkingBlacklistPO> parkingBlacklistPOS = illegalParkingBlacklistRepository.lambdaQuery()
                .le(IllegalParkingBlacklistPO::getCreateTime, timeCycle.getBegin())
                .list();

        if (CollectionUtils.isEmpty(parkingBlacklistPOS)) {
            log.info("未获取到需要清理的违停黑名单");
            return;
        }
        List<Long> blackListIds = new ArrayList<>();
        Set<String> plateNumbers = new HashSet<>();
        for (var parkingBlacklistPO : parkingBlacklistPOS) {
            blackListIds.add(parkingBlacklistPO.getId());
            plateNumbers.add(parkingBlacklistPO.getPlateNumber());
        }
        //删除黑名单
        illegalParkingBlacklistCommandService.removeBlacklistBath(blackListIds);
        //移除扣分记录
        illegalParkingCommandService.removeIllegalParkingBath(plateNumbers);
        //删除违停记录
        illegalParkingCommandService.removeIllegalParkingBath(plateNumbers);

    }
}
