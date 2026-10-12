package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/10 14:24
 * @Description:
 */

@Data
public class VechicleApprovalVO {

    /**
     * 用户id
     */
    private List<Long> userId;

    /**
     * 新增车牌信息
     */
    private List<VechicleNo> vehicleNos;

    /**
     * 停车场
     */
    private String parkingLot;

    private String vehicleNo;

    @Data
    public static class VechicleNo {

        /**
         * 车牌号
         */
        private String vehicleNo;
    }
}
