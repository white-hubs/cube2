package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.commandgetway.command.VehiclePenaltyCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingRecordRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.valueobj.IllegalParkingApprovalInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.SubmitInitFormCommand;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.guhecloud.cube.biz.subsystem.parking.server.flow.enumeration.VechicleApprovalEnum.ILLEGAL_PARKING;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:30
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingCommandService {

    private final IllegalParkingRecordRepository illegalParkingRecordRepository;
    private final VehiclePenaltyCommandService vehiclePenaltyCommandService;
    private final FlowClient flowClient;
    private final SatSendClient satSendClient;
    private final VehicleInfoService vehicleInfoService;

    @Transactional(rollbackFor = Exception.class)
    public String submitApproval(SubmitInitFormCommand command) {
        //解析表单信息
        FormDataParam data = new FormDataParam(command.getFormDefinitionId(), command.getData());
        String fromString;
        IllegalParkingApprovalInfo parkingApprovalInfo;
        try {
            fromString = flowClient.formParse(data);
            parkingApprovalInfo = JsonUtils.deserialize(fromString, IllegalParkingApprovalInfo.class);
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        //前置校验
        this.verification(parkingApprovalInfo);
        //存入违停记录
        IllegalParkingRecordPO illegalParkingRecordPO = IllegalParkingRecordPO
                .builder()
                .id(IdWorker.getId())
                .code(flowClient.getFormNo("WT"))
                .name(SecurityContextHolder.getPrincipal().getName() + "发起的" + ILLEGAL_PARKING.getDesc())
                .illegalLevel(parkingApprovalInfo.getIllegalLevel())
                .plateNumber(parkingApprovalInfo.getVehicleNo())
                .ownerName(parkingApprovalInfo.getOwnerName())
                .ownerPhone(parkingApprovalInfo.getOwnerPhone())
                .build();
        //发起流程
        command.setBackLogTitle(illegalParkingRecordPO.getName());
        command.setFormNo(illegalParkingRecordPO.getCode());
        String processInstanceId = flowClient.submitFirst(command);
        illegalParkingRecordPO.setProcessInstanceId(processInstanceId);
        illegalParkingRecordRepository.save(illegalParkingRecordPO);
        VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
        vehicleInfoVO.setVehicleNo(parkingApprovalInfo.getVehicleNo());
        vehicleInfoVO.setCustomerName(parkingApprovalInfo.getOwnerName());
        vehicleInfoVO.setCustomerMobile(parkingApprovalInfo.getOwnerPhone());
        vehicleInfoService.saveNotExist(vehicleInfoVO);
        return processInstanceId;
    }

    private void verification(IllegalParkingApprovalInfo parkingApprovalInfo) {
        Assert.isParameterError(Objects.isNull(parkingApprovalInfo), "流程表单信息未配置");
        Assert.isParameterError(Objects.isNull(parkingApprovalInfo.getIllegalLevel()), "流程配置违停等级未配置");
        Assert.isParameterError(Objects.isNull(parkingApprovalInfo.getVehicleNo()), "车牌号不能为空");
        Assert.isParameterError(Objects.isNull(parkingApprovalInfo.getOwnerName()), "车主姓名不能为空");
        Assert.isParameterError(Objects.isNull(parkingApprovalInfo.getOwnerPhone()), "车主电话不能为空");
    }

    public void businessOperation(ProcessCompletedMessage message) {
        IllegalParkingApprovalInfo approvalInfo = JsonUtils.deserialize(message.getData(), IllegalParkingApprovalInfo.class);
        IllegalParkingRecordPO parkingRecordPO = illegalParkingRecordRepository.lambdaQuery()
                .eq(IllegalParkingRecordPO::getProcessInstanceId, message.getProcessInstanceId())
                .last("limit 1")
                .one();
        //更新违停记录
        if (Objects.nonNull(parkingRecordPO)) {
            parkingRecordPO.setIllegalLevel(approvalInfo.getIllegalLevel());
            parkingRecordPO.setPlateNumber(approvalInfo.getVehicleNo());
            parkingRecordPO.setOwnerName(approvalInfo.getOwnerName());
            parkingRecordPO.setOwnerPhone(approvalInfo.getOwnerPhone());
            illegalParkingRecordRepository.updateById(parkingRecordPO);
        } else {
            parkingRecordPO = new IllegalParkingRecordPO();
            parkingRecordPO.setProcessInstanceId(message.getProcessInstanceId());
            parkingRecordPO.setId(IdWorker.getId());
            parkingRecordPO.setIllegalLevel(approvalInfo.getIllegalLevel());
            parkingRecordPO.setPlateNumber(approvalInfo.getVehicleNo());
            parkingRecordPO.setOwnerName(approvalInfo.getOwnerName());
            parkingRecordPO.setOwnerPhone(approvalInfo.getOwnerPhone());
            illegalParkingRecordRepository.save(parkingRecordPO);
        }

        //发送消息提醒车主
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("ownerName", parkingRecordPO.getOwnerName());
        paramMap.put("illegalTime", DateUtils.formatDate(parkingRecordPO.getCreateTime()));
        List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>();
        MsgSendTargetParam sendTargetParam = MsgSendTargetParam.builder()
                .userId(Long.valueOf(parkingRecordPO.getOwnerPhone())).userName(parkingRecordPO.getOwnerName())
                .userPhone(parkingRecordPO.getOwnerPhone()).receiver(parkingRecordPO.getOwnerPhone()).build();
        satMsgChannelSendParams.add(new SatMsgChannelSendParam(null, ChannelEnum.CHANNEL_SMS, SessionContextHolder.getRequester()
                .getTenant(), Collections.singletonList(sendTargetParam)));
        try {
            this.satSendClient.sendMsg(SatMsgSendParam.builder().businessId(String.valueOf(parkingRecordPO.getId()))
                                               .msgSceneCode(ILLEGAL_PARKING.name()).msgParams(paramMap)
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .tenant(SessionContextHolder.getRequester().getTenant()).build());
            log.info("消息:{},发送成功", "ILLEGAL_PARKING");
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
        //进行扣分处理
        VehiclePenaltyCommand command = new VehiclePenaltyCommand();
        command.setIllegalParkingRecord(parkingRecordPO);
        command.setPlateColor(approvalInfo.getPlateColor());
        command.setParkingLot(approvalInfo.getParkingLot());
        command.setPlateNumber(approvalInfo.getVehicleNo());
        command.setIllegalTime(message.getStartTime());
        command.setIllegalLevel(
                StringUtils.isNotBlank(approvalInfo.getIllegalLevel()) ? IllegalLevelEnum.valueOf(approvalInfo.getIllegalLevel()) : null);
        vehiclePenaltyCommandService.vehiclePenalty(command);

    }

    @Transactional(rollbackFor = Exception.class)
    public void removeIllegalParkingBath(Set<String> plateNumbers) {
        List<IllegalParkingRecordPO> parkingRecordPOS = illegalParkingRecordRepository.lambdaQuery()
                .in(IllegalParkingRecordPO::getPlateNumber, plateNumbers)
                .list();
        if (CollectionUtils.isEmpty(parkingRecordPOS)) {
            return;
        }
        DatabaseContextHolder.softDelete(IllegalParkingRecordPO.class, parkingRecordPOS);
        Set<String> processInsIds = parkingRecordPOS.stream().map(IllegalParkingRecordPO::getProcessInstanceId).collect(Collectors.toSet());
        Set<Long> idList = parkingRecordPOS.stream().map(IllegalParkingRecordPO::getId).collect(Collectors.toSet());

        vehiclePenaltyCommandService.removeVehiclePenaltyBath(idList);
        flowClient.deleteFormFlow(processInsIds);
    }
}
