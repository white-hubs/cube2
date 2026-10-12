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
public class BsgoalParkInfoResponse {
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private List<ParkingInfo> data;

    @Data
    public static class ParkingInfo {
        /**
         * 车辆所在车场ID
         */
        @JsonProperty("ParkingID")
        private String parkingId;
        /**
         * 车辆所在车场描述
         */
        @JsonProperty("ParkName")
        private String parkName;
        /**
         * 区域ID
         */
        @JsonProperty("AreaID")
        private String areaId;
        /**
         * 区域名称
         */
        @JsonProperty("AreaName")
        private String areaName;
        /**
         * 车位数
         */
        @JsonProperty("CarNum")
        private String carNum;
        /**
         * 上级区域ID
         */
        @JsonProperty("MasterID")
        private String masterId;
    }
}
