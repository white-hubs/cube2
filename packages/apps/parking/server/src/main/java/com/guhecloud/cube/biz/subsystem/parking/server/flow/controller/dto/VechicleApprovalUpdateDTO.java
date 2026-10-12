package com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto;

import annotation.FormField;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.base.BaseVechicleApproval;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 14:22
 * @Description:
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class VechicleApprovalUpdateDTO extends BaseVechicleApproval {

    @FormField(label = "车辆变更信息")
    private List<VechicleChangeDTO> vechicleChanges;
    @FormField(label = "车辆变更信息/变更前车牌号")
    private String beforeVehicleNo;
    @FormField(label = "车辆变更信息/变更后车牌号")
    private String afterVehicleNo;

    @Data
    public static class VechicleChangeDTO {
        @FormField(label = "车辆变更信息/变更前车牌号")
        private String beforeVehicleNo;
        @FormField(label = "车辆变更信息/变更后车牌号")
        private String afterVehicleNo;
    }
}
