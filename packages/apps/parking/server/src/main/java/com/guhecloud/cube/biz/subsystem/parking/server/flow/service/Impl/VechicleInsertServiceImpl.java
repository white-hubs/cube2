package com.guhecloud.cube.biz.subsystem.parking.server.flow.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.base.BaseVechicleApproval;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleApprovalDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleApprovalUpdateDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto.VechicleDeleteDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.enumeration.VechicleApprovalEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.service.VehicleApprovalService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import instance.command.SubmitInitFormCommand;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 10:41
 * @Description:
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VechicleInsertServiceImpl implements VehicleApprovalService {
    private final VehicleInfoService vehicleInfoService;
    private final FlowClient flowClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitApproval(SubmitInitFormCommand command) {
        Optional<VechicleApprovalEnum> approvalEnum = Arrays.stream(VechicleApprovalEnum.values())
                .filter(t -> t.name().equals(command.getBusinessType()))
                .findFirst();
        Assert.isNotFound(approvalEnum.isEmpty(), "未找到指定审批流程");
        //解析表单信息
        FormDataParam data = new FormDataParam(command.getFormDefinitionId(), command.getData());
        String fromString;
        Object vechicleDTO = null;
        try {
            fromString = flowClient.formParse(data);
            vechicleDTO = JSONObject.parseObject(fromString, approvalEnum.get().getClazz());
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        //前置校验
        this.verification((BaseVechicleApproval) vechicleDTO);
        switch (approvalEnum.get()) {
            case VECHICLE_INSERT:
                insertVerification((VechicleApprovalDTO) vechicleDTO);
                break;
            case VECHICLE_UPDATE:
                updateVerification((VechicleApprovalUpdateDTO) vechicleDTO);
                break;
            case VECHICLE_DELETE:
                deleteVerification((VechicleDeleteDTO) vechicleDTO);
                break;
        }
        //发起流程
        return flowClient.submitFirst(command);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void businessOperation(ProcessCompletedMessage message) {
        Optional<VechicleApprovalEnum> approvalEnum = Arrays.stream(VechicleApprovalEnum.values())
                .filter(t -> t.name().equals(message.getBusinessId().getBusinessType()))
                .findFirst();
        Object vechicleDTO;
        try {
            vechicleDTO = JSONObject.parseObject(message.getData(), approvalEnum.get().getClazz());
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        switch (approvalEnum.get()) {
            case VECHICLE_INSERT:
                insertVechicleInfo((VechicleApprovalDTO) vechicleDTO);
                break;
            case VECHICLE_UPDATE:
                List<VehicleInfo> vehicleInfos = updateVerification((VechicleApprovalUpdateDTO) vechicleDTO);
                vehicleInfoService.updateBatchById(vehicleInfos);
                break;
            case VECHICLE_DELETE:
                List<VehicleInfo> vehicleInfos1 = deleteVerification((VechicleDeleteDTO) vechicleDTO);
                vehicleInfoService.updateBatchById(vehicleInfos1);
                break;
        }
    }

    @Override
    public List<String> getVechicleById(Long id) {
        List<VehicleInfo> vehicleInfos = vehicleInfoService.lambdaQuery()
                .eq(VehicleInfo::getCustomerId, id)
                .list();
        if (CollectionUtils.isEmpty(vehicleInfos)) {
            return new ArrayList<>();
        }
        return vehicleInfos.stream().map(VehicleInfo::getVehicleNo).collect(Collectors.toList());
    }

    /**
     * 车辆删除校验
     *
     * @param vechicleApprovalDTO
     */
    private List<VehicleInfo> deleteVerification(VechicleDeleteDTO vechicleApprovalDTO) {
        Assert.isNotAcceptable(CollectionUtils.isEmpty(vechicleApprovalDTO.getVehicleNos()), "流程配置未配置车牌号信息");
        List<VehicleInfo> vehicleInfos = vehicleInfoService.lambdaQuery()
                .in(VehicleInfo::getVehicleNo, vechicleApprovalDTO.getVehicleNos().stream()
                        .map(VechicleApprovalDTO.VechicleNo::getVehicleNo).collect(Collectors.toList()))
                .list();

        Assert.isNotFound(CollectionUtils.isEmpty(vehicleInfos), "车牌号:" + vehicleInfos.stream()
                .map(VehicleInfo::getVehicleNo).collect(Collectors.toList()) + "不存在,无法删除");
        for (VehicleInfo vehicleInfo : vehicleInfos) {
            vehicleInfo.setDeleted(true);
        }
        return vehicleInfos;
    }

    /**
     * 车辆更新校验
     *
     * @param vechicleApprovalDTO
     */
    private List<VehicleInfo> updateVerification(VechicleApprovalUpdateDTO vechicleApprovalDTO) {
        Assert.isParameterError(CollectionUtils.isEmpty(vechicleApprovalDTO.getVechicleChanges()), "流程配置未配置变更车牌号信息");
        Set<String> sameVehicleNos = new HashSet<>();
        Map<String, String> updateMap = new HashMap<>();
        for (VechicleApprovalUpdateDTO.VechicleChangeDTO vechicleChange : vechicleApprovalDTO.getVechicleChanges()) {
            Assert.isParameterError(StringUtils.isBlank(vechicleChange.getBeforeVehicleNo()), "流程配置未配置变更前车牌号信息");
            Assert.isParameterError(StringUtils.isBlank(vechicleChange.getAfterVehicleNo()), "流程配置未配置变更后车牌号信息");
            Assert.isConflict(sameVehicleNos.contains(vechicleChange.getBeforeVehicleNo()), "存在相同车牌号");
            sameVehicleNos.add(vechicleChange.getBeforeVehicleNo());
            updateMap.put(vechicleChange.getBeforeVehicleNo(), vechicleChange.getAfterVehicleNo());
        }
        List<VehicleInfo> vehicleInfos = vehicleInfoService.lambdaQuery()
                .in(VehicleInfo::getVehicleNo, updateMap.keySet())
                .eq(VehicleInfo::getCustomerId, vechicleApprovalDTO.getUserId())
                .list();
        Assert.isNotFound(CollectionUtils.isEmpty(vehicleInfos), "车牌号信息不存在不能更改");
        vehicleInfos.forEach(t -> {
            t.setVehicleNo(updateMap.get(t.getVehicleNo()));
        });
        return vehicleInfos;
    }

    /**
     * 车辆新增校验
     *
     * @param vechicleApprovalDTO
     */

    private void insertVerification(VechicleApprovalDTO vechicleApprovalDTO) {
        Assert.isParameterError(CollectionUtils.isEmpty(vechicleApprovalDTO.getVehicleNos()), "流程配置未配置车牌号信息");
        List<String> vehicleNos = Optional.ofNullable(vehicleInfoService.lambdaQuery()
                                                              .select(VehicleInfo::getVehicleNo)
                                                              .in(VehicleInfo::getVehicleNo, vechicleApprovalDTO.getVehicleNos().stream()
                                                                      .map(VechicleApprovalDTO.VechicleNo::getVehicleNo)
                                                                      .collect(Collectors.toList()))
                                                              .list()).orElse(new ArrayList<>()).stream().map(VehicleInfo::getVehicleNo)
                .collect(Collectors.toList());
        Assert.isConflict(!CollectionUtils.isEmpty(vehicleNos), "车牌号:" + vehicleNos + "已存在");
    }

    private void insertVechicleInfo(VechicleApprovalDTO vechicleApprovalDTO) {
        List<VehicleInfo> vehicleInfoList = vechicleApprovalDTO.getVehicleNos().stream().map(t -> {
            VehicleInfo vehicleInfo = new VehicleInfo();
            vehicleInfo.setVehicleNo(t.getVehicleNo());
            vehicleInfo.setCustomerId(vechicleApprovalDTO.getUserId().get(0));
            return vehicleInfo;
        }).collect(Collectors.toList());
        vehicleInfoService.saveBatch(vehicleInfoList);
    }

    /**
     * 前置公共校验
     *
     * @param baseVechicleApproval
     */
    private void verification(BaseVechicleApproval baseVechicleApproval) {
        Assert.isParameterError(CollectionUtils.isEmpty(baseVechicleApproval.getUserId()), "流程配置车主姓名未配置");
    }
}
