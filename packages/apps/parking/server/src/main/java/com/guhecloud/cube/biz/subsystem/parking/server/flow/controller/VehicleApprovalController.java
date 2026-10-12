package com.guhecloud.cube.biz.subsystem.parking.server.flow.controller;

import com.guhecloud.cube.biz.subsystem.parking.server.flow.enumeration.VechicleApprovalEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.service.VehicleApprovalService;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.CommonEnumVO;
import vo.FormFieldVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 10:07
 * @Description: 车辆审批流程Api
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/vehicle/approval")
public class VehicleApprovalController {
    private final VehicleApprovalService vehicleApprovalService;

    /**
     * 发起审批
     */
    @PostMapping("/submit")
    public String submitApproval(@RequestBody SubmitInitFormCommand submitInitFormCommand) {
        return vehicleApprovalService.submitApproval(submitInitFormCommand);
    }

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(VechicleApprovalEnum.values()).map(e -> CommonEnumVO.builder()
                        .code(e.name()).name(e.getDesc()).build())
                .collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/biz/form/field")
    public List<FormFieldVO> getFormField(VechicleApprovalEnum type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }

    /**
     * 获取用户车牌车牌号
     */

    @GetMapping("/vechicle/no")
    public List<String> getVechicleById(@RequestParam(value = "id") Long id) {
        return vehicleApprovalService.getVechicleById(id);
    }
}
