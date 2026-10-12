package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.IllegalParkingBlacklistCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.aggregates.IllegalParkingBlacklistInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalTimeCycleEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.event.IllegalParkingBlacklistEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingBlacklistRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters.IllegalParkingBlacklistConverts;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:42
 * @Description:
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingBlacklistCommandService {
    public static final String ILLEGAL_PARKING_BLACKLIST = "ILLEGAL_PARKING_BLACKLIST";
    private final IllegalParkingBlacklistRepository illegalParkingBlacklistRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SatSendClient satSendClient;
    private final VehicleInfoService vehicleInfoService;

    @Transactional(rollbackFor = Exception.class)
    public Boolean createBlacklist(IllegalParkingBlacklistCommand command) {
        //保存车牌号
        VehicleInfo vehicleInfo = vehicleInfoService.lambdaQuery()
                .eq(VehicleInfo::getVehicleNo, command.getPlateNumber())
                .last("limit 1")
                .one();
        if (Objects.nonNull(vehicleInfo)) {
            vehicleInfo.setIsBlacklist(true);
            vehicleInfoService.updateById(vehicleInfo);
        } else {
            VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
            vehicleInfoVO.setVehicleNo(command.getPlateNumber());
            vehicleInfoVO.setCustomerName(command.getOwner());
            vehicleInfoVO.setIsBlacklist(true);
            vehicleInfoVO.setCustomerMobile(command.getPhone());
            vehicleInfoService.saveNotExist(vehicleInfoVO);
        }
        Period timeCycle = IllegalTimeCycleEnum.getIllegalTimeCycle(command.getCycle());
        boolean exists = illegalParkingBlacklistRepository.lambdaQuery()
                .eq(IllegalParkingBlacklistPO::getPlateNumber, command.getPlateNumber())
                .exists();
        Assert.isParameterError(exists, "该车牌号已存在黑名单中");
        IllegalParkingBlacklistPO parkingBlacklistPO = IllegalParkingBlacklistPO.builder()
                .id(IdWorker.getId())
                .plateNumber(command.getPlateNumber())
                .owner(command.getOwner())
                .phone(command.getPhone())
                .reason(command.getReason())
                .parkingLot(command.getParkingLot())
                .startTime(timeCycle.getBegin())
                .endTime(timeCycle.getEnd())
                .build();
        illegalParkingBlacklistRepository.save(parkingBlacklistPO);
        //发布车辆黑名单事件
        List<IllegalParkingBlacklistInfo> illegalParkingBlacklistInfos = IllegalParkingBlacklistConverts.convertToBlacklist(Collections.singletonList(parkingBlacklistPO), true);
        illegalParkingBlacklistInfos.forEach(t -> {
            t.setPlateColor(
                    Objects.nonNull(vehicleInfo.getVehicleNoColor()) ? vehicleInfo.getVehicleNoColor().name() : null);
        });
        eventPublisher.publishEvent(new IllegalParkingBlacklistEvent(this, illegalParkingBlacklistInfos));
        //纳入黑名单提醒车主
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("owner", parkingBlacklistPO.getOwner());
        paramMap.put("time", DateUtils.formatDate(parkingBlacklistPO.getCreateTime()));
        paramMap.put("reason", parkingBlacklistPO.getReason());
        List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>();
        MsgSendTargetParam sendTargetParam = MsgSendTargetParam.builder()
                .userId(Long.valueOf(parkingBlacklistPO.getPhone())).userName(parkingBlacklistPO.getOwner())
                .userPhone(parkingBlacklistPO.getPhone()).receiver(parkingBlacklistPO.getPhone()).build();
        satMsgChannelSendParams.add(new SatMsgChannelSendParam(null, ChannelEnum.CHANNEL_SMS, SessionContextHolder.getRequester()
                .getTenant(), Collections.singletonList(sendTargetParam)));
        try {
            this.satSendClient.sendMsg(SatMsgSendParam.builder().businessId(String.valueOf(parkingBlacklistPO.getId()))
                                               .msgSceneCode(ILLEGAL_PARKING_BLACKLIST).msgParams(paramMap)
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .tenant(SessionContextHolder.getRequester().getTenant()).build());
            log.info("消息:{},发送成功", ILLEGAL_PARKING_BLACKLIST);
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBlacklist(Long id) {
        this.removeBlacklistBath(Collections.singletonList(id));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeBlacklistBath(List<Long> blackListIds) {
        List<IllegalParkingBlacklistPO> parkingBlacklistPOS = illegalParkingBlacklistRepository.
                lambdaQuery().in(IllegalParkingBlacklistPO::getId, blackListIds).list();
        Assert.isNotFound(CollectionUtils.isEmpty(parkingBlacklistPOS), "车辆黑名单不存在");
        Set<String> plateNumberList = parkingBlacklistPOS.stream().map(IllegalParkingBlacklistPO::getPlateNumber).
                collect(Collectors.toSet());
        List<VehicleInfo> vehicleInfos = vehicleInfoService.lambdaQuery()
                .in(VehicleInfo::getVehicleNo, plateNumberList)
                .list();
        //更新车辆状态
        vehicleInfos.forEach(t -> {
            t.setIsBlacklist(false);
        });
        vehicleInfoService.updateBatchById(vehicleInfos);
        Map<String, VehicleNoColorEnum> vehicleNoColorEnumMap = vehicleInfos.stream()
                .filter(t -> Objects.nonNull(t.getVehicleNoColor()))
                .collect(Collectors.toMap(VehicleInfo::getVehicleNo, VehicleInfo::getVehicleNoColor));
        DatabaseContextHolder.softDelete(IllegalParkingBlacklistPO.class, parkingBlacklistPOS);
        //发布车辆黑名单事件
        List<IllegalParkingBlacklistInfo> illegalParkingBlacklistInfos = IllegalParkingBlacklistConverts.convertToBlacklist(parkingBlacklistPOS, false);
        illegalParkingBlacklistInfos.forEach(t -> {
            t.setPlateColor(vehicleNoColorEnumMap.getOrDefault(t.getPlateNumber(), VehicleNoColorEnum.UNKNOWN).name());
        });
        eventPublisher.publishEvent(new IllegalParkingBlacklistEvent(this, illegalParkingBlacklistInfos));

    }
}
