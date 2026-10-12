package com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.dto;

import annotation.FormField;
import com.guhecloud.cube.biz.subsystem.parking.server.flow.controller.base.BaseVechicleApproval;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 14:24
 * @Description:
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class VechicleApprovalDTO extends BaseVechicleApproval {

    @FormField(label = "新增车牌信息")
    private List<VechicleNo> vehicleNos;
    @FormField(label = "新增车牌/车牌号")
    private String vehicleNo;

    @Data
    public static class VechicleNo {
        @FormField(label = "车牌号")
        private String vehicleNo;
    }
}
