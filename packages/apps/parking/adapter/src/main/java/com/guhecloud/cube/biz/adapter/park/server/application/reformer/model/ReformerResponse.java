package com.guhecloud.cube.biz.adapter.park.server.application.reformer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReformerResponse {

    /**
     * 返回值，0 为成功，其他为失败
     */
    private Integer resCode;
    /**
     * 返回说明
     */
    private String resMsg;
    /**
     * 总车位
     */
    private Integer totalNum;
    /**
     * 总已停车位
     */
    private Integer totalStopNum;
    /**
     * 总剩余车位
     */
    private Integer totalRemainNum;
    /**
     * 车场 ID
     */
    private String parkID;
    /**
     * 车场名称
     */
    private String parkName;
    /**
     * 车场收费标准
     */
    private String chargeRuleDesc;
    /**
     * 区域余位信息
     */
    private List<ParkingInfo> parkingLotInfo;

    @Data
    public static class ParkingInfo {
        /**
         * 区域 ID
         */
        private Integer parkingLotId;
        /**
         * 区域名称
         */
        private String parkingLotName;
        /**
         * 总车位
         */
        private Integer totalNum;
        /**
         * 总已停车位
         */
        private Integer totalStopNum;
        /**
         * 总剩余车位
         */
        private Integer totalRemainNum;
    }
}
