package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BsgoalCarNumberResponse {
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private ParkingCarNumber data;

    @Data
    public static class ParkingCarNumber {
        /**
         * 合计已占用车位车位数
         */
        @JsonProperty("PresentSpace")
        private Integer presentSpace;
        /**
         * 合计剩余车位数
         */
        @JsonProperty("RemainingSpace")
        private Integer remainingSpace;
        /**
         * 合计总车位数
         */
        @JsonProperty("TotalSpace")
        private Integer totalSpace;
        /**
         * 车辆剩余位数详情信息
         */
        @JsonProperty("List")
        private List<AreaVehicleCount> list;
    }

    @Data
    public static class AreaVehicleCount {
        /**
         * 区域ID
         */
        @JsonProperty("AreaID")
        private String areaID;
        /**
         * 区域名称
         */
        @JsonProperty("AreaName")
        private String areaName;
        /**
         * 已占用车位车位数
         */
        @JsonProperty("PresentSpace")
        private Integer presentSpace;
        /**
         * 返回剩余车位数
         */
        @JsonProperty("RemainingSpace")
        private Integer remainingSpace;
        /**
         * 总车位数
         */
        @JsonProperty("TotalSpace")
        private Integer totalSpace;
    }
}
