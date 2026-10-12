package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingBlacklistCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.VehiclePenaltyCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.BlacklistConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.service.IllegalParkingConfigService;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalTimeCycleEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.VehiclePenaltyRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.VehiclePenaltyRecordRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.BlacklistConfigVO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingConfigVO;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 14:18
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehiclePenaltyCommandService {
    private final IllegalParkingConfigService illegalParkingConfigService;
    private final BlacklistConfigService blacklistConfigService;
    private final VehiclePenaltyRecordRepository vehiclePenaltyRecordRepository;
    private final IllegalParkingBlacklistCommandService illegalParkingBlacklistCommandService;

    @Transactional(rollbackFor = Exception.class)
    public void vehiclePenalty(VehiclePenaltyCommand command) {

        //获取指定等级的扣分规则
        IllegalParkingConfigVO illegalParkingConfig = Optional
                .ofNullable(illegalParkingConfigService.getIllegalParkingConfig(command.getIllegalLevel()))
                .orElse(new IllegalParkingConfigVO());
        VehiclePenaltyRecordPO recordPO = new VehiclePenaltyRecordPO();
        recordPO.setId(IdWorker.getId());
        recordPO.setIllegalRecordId(command.getIllegalParkingRecord().getId());
        recordPO.setPlateNumber(command.getPlateNumber());
        recordPO.setIllegalLevel(Objects.nonNull(command.getIllegalLevel()) ? command.getIllegalLevel().name() : null);
        recordPO.setViolationScore(illegalParkingConfig.getScore());
        recordPO.setIllegalTime(command.getIllegalTime());
        vehiclePenaltyRecordRepository.save(recordPO);
        //获取周期内的违停记录
        Period timeCycle = IllegalTimeCycleEnum.getIllegalTimeCycle(illegalParkingConfig.getTimeCycle());
        List<VehiclePenaltyRecordPO> penaltyRecordPOS = vehiclePenaltyRecordRepository.lambdaQuery()
                .between(VehiclePenaltyRecordPO::getIllegalTime, timeCycle.getBegin(), timeCycle.getEnd())
                .eq(VehiclePenaltyRecordPO::getPlateNumber, command.getPlateNumber())
                .list();
        BigDecimal reduce = penaltyRecordPOS.stream().map(VehiclePenaltyRecordPO::getViolationScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //判断是否需要加入黑名单
        BlacklistConfigVO config = blacklistConfigService.getConfig();
        if (Objects.nonNull(config.getThresholdValue()) && reduce.compareTo(config.getThresholdValue()) >= 0) {
            IllegalParkingBlacklistCommand blacklistCommand = new IllegalParkingBlacklistCommand();
            blacklistCommand.setPlateNumber(command.getPlateNumber());
            blacklistCommand.setPlateColor(command.getPlateColor());
            blacklistCommand.setOwner(command.getIllegalParkingRecord().getOwnerName());
            blacklistCommand.setPhone(command.getIllegalParkingRecord().getOwnerPhone());
            blacklistCommand.setCycle(illegalParkingConfig.getTimeCycle());
            blacklistCommand.setParkingLot(command.getParkingLot());
            blacklistCommand.setReason("累计违章停车扣分已达加入黑名单的要求");
            illegalParkingBlacklistCommandService.createBlacklist(blacklistCommand);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void removeVehiclePenaltyBath(Set<Long> idList) {
        vehiclePenaltyRecordRepository.lambdaUpdate()
                .set(VehiclePenaltyRecordPO::getDeleted, System.currentTimeMillis())
                .in(VehiclePenaltyRecordPO::getId, idList)
                .update();
    }
}
