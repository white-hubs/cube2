package com.guhecloud.cube.srv.gen.securitymanagement.server.information.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceInfoVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.InfoScreenScheduleAssocMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationPlanDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.InformationPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.InformationPlanService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.InformationPlanVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 开关屏配置管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InformationPlanController {
    private final InformationPlanService infoPlanService;
    private final InfoScreenScheduleAssocMapper infoScreenScheduleAssocMapper;

    /**
     * 保存开关屏配置
     */
    @PutMapping("/information/plan/saveInformationPlan")
    public void saveInformationPlan(@RequestBody InformationPlanCmd cmd) {
        InformationPlanDTO param = InformationPlanDTO.builder()
                .id(cmd.getId())
                .planName(cmd.getPlanName())
                .planStartTime(cmd.getPlanStartTime())
                .planEndTime(cmd.getPlanEndTime())
                .openTime(cmd.getOpenTime())
                .closeTime(cmd.getCloseTime())
                .isEnable(cmd.getIsEnable())
                .openJobTaskId(cmd.getOpenJobTaskId())
                .securityDevices(cmd.getSecurityDevices())
                .closeJobTaskId(cmd.getCloseJobTaskId()).build();
        if (!CollectionUtils.isEmpty(cmd.getExecutionCycle())) {
            param.setExecutionCycle(String.join(",", cmd.getExecutionCycle()));
        }
        infoPlanService.saveInformationPlan(param);
    }

    /**
     * 根据id获取开关屏配置管理
     */
    @GetMapping("/information/plan/getInformationPlanInfo")
    public InformationPlanVO getInformationPlanInfo(@RequestParam(value = "id") String id) {
        InformationPlanDTO infoPlanDTO = infoPlanService.getMaterialsByProgramIds(Long.parseLong(id));
        InformationPlanVO vo = InformationPlanVO.builder()
                .id(infoPlanDTO.getId())
                .planName(infoPlanDTO.getPlanName())
                .planStartTime(infoPlanDTO.getPlanStartTime())
                .planEndTime(infoPlanDTO.getPlanEndTime())
                .openTime(infoPlanDTO.getOpenTime())
                .closeTime(infoPlanDTO.getCloseTime())
                .isEnable(infoPlanDTO.getIsEnable())
                .openJobTaskId(infoPlanDTO.getOpenJobTaskId())
                .closeJobTaskId(infoPlanDTO.getCloseJobTaskId())
                .securityDevices(infoPlanDTO.getSecurityDevices())
                .build();
        if (StringUtils.isNotEmpty(infoPlanDTO.getExecutionCycle())) {
            vo.setExecutionCycle(Arrays.asList(infoPlanDTO.getExecutionCycle().split(",")));
        }
        return vo;
    }

    /**
     * 开关屏配置管理列表分页
     */
    @GetMapping("/information/plan/getInformationPlanPage")
    public Pagination<InformationPlanVO> getInformationPlanPage(InformationPlanParam param) {
        return infoPlanService.getInformationPlanPage(param);
    }

    /**
     * 删除开关屏配置管理
     */
    @DeleteMapping("/information/plan/deleteInformationPlan")
    public void deleteInformationPlan(@RequestParam(value = "id") String id) {
        infoPlanService.deleteInformationPlan(Long.parseLong(id));
    }

    /**
     * 根据设备id 删除开关屏配置关联的设备
     */
    @DeleteMapping("/information/plan/deleteInfoScreenScheduleAssoc")
    public void deleteInfoScreenScheduleAssoc(@RequestParam(value = "deviceId") String deviceId) {
        infoScreenScheduleAssocMapper.deleteInfoScreenScheduleAssoc(Long.parseLong(deviceId));
    }

    /**
     * 启动开关屏配置管理
     */
    @GetMapping("/information/plan/startInformationPlan")
    public void startInformationPlan(@RequestParam(value = "id") String id) {
        infoPlanService.updateInformationPlanStatus(Long.parseLong(id), 1);
    }

    /**
     * 停止开关屏配置管理
     */
    @GetMapping("/information/plan/stopInformationPlan")
    public void stopInformationPlan(@RequestParam(value = "id") String id) {
        infoPlanService.updateInformationPlanStatus(Long.parseLong(id), 0);
    }

    /**
     * 获取未被开关屏计划关联的设备列表
     */
    @GetMapping("/information/plan/getUnusedDevicePage")
    public Pagination<SecurityDeviceInfoVO> getUnusedDevicePage(InformationPlanParam param) {
        param.setApplicationCode("INFORMATION_PUBLISH");
        return infoPlanService.getUnusedDevicePage(param);
    }

}
