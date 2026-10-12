package com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto;

import annotation.FormField;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.base.BaseVechicleApproval;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 11:50
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VechicleDeleteDTO extends BaseVechicleApproval {

    @FormField(label = "删除车牌信息")
    private List<VechicleApprovalDTO.VechicleNo> vehicleNos;
    @FormField(label = "删除车牌信息/车牌号")
    private String vehicleNo;

}
