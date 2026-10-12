package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 14:22
 * @Description:
 */

@Data
public class VechicleApprovalUpdateVO {
    /**
     * 用户id
     */
    private List<Long> userId;

    /**
     * 车辆变更信息
     */
    private List<VechicleChangeDTO> vechicleChanges;
    /**
     * 车辆变更信息/变更前车牌号
     */
    private String beforeVehicleNo;
    /**
     * 车辆变更信息/变更后车牌号
     */
    private String afterVehicleNo;

    @Data
    public static class VechicleChangeDTO {

        /**
         * 车辆变更信息/变更前车牌号
         */
        private String beforeVehicleNo;

        /**
         * 车辆变更信息/变更后车牌号
         */
        private String afterVehicleNo;
    }
}
