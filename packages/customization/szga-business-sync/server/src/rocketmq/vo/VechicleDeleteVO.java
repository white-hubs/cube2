package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 11:50
 * @Description:
 */

@Data
public class VechicleDeleteVO {

    /**
     * 用户id
     */
    private List<Long> userId;

    /**
     * 删除车牌信息
     */
    private List<VechicleDeleteVO.VechicleNo> vehicleNos;

    /**
     * 删除车牌信息/车牌号
     */
    private String vehicleNo;

    @Data
    public static class VechicleNo {

        /**
         * 车牌号
         */
        private String vehicleNo;
    }
}
