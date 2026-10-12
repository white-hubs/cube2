package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BsgoalParkInOutResponse {
    /**
     * 标识(0成功,-1失败) ，其他状态码详细见返回状态说明
     */
    private Integer status;
    /**
     * 消息
     */
    private String message;
    /**
     * 记录对象
     */
    @JsonProperty("Data")
    private Record data;

    @Data
    public static class Record {
        /**
         * 总记录数
         */
        @JsonProperty("TotalCount")
        private Integer totalCount;
        /**
         * 记录对象
         */
        @JsonProperty("List")
        private List<RecordInfo> list;
    }

    @Data
    public static class RecordInfo {
        /**
         * 进场记录ID
         */
        @JsonProperty("RecordID")
        private String recordID;
        /**
         * 车场id
         */
        @JsonProperty("ParkingID")
        private String parkingID;
        /**
         * 进出方向，1: 进  2: 出
         */
        @JsonProperty("InOrOutFlag")
        private Integer inOrOutFlag;
        /**
         * 车牌
         */
        @JsonProperty("PlateNumber")
        private String plateNumber;
        /**
         * 车辆类型ID
         */
        @JsonProperty("CarTypeID")
        private String carTypeID;
        /**
         * 车辆类型名称
         */
        @JsonProperty("CarTypeName")
        private String carTypeName;
        /**
         * 收费类别ID
         */
        @JsonProperty("CardTypeID")
        private String cardTypeID;
        /**
         * 收费类别名称
         */
        @JsonProperty("CardTypeName")
        private String cardTypeName;
        /**
         * 是否出场；0-未出场；1-已出场
         */
        @JsonProperty("IsExit")
        private Integer isExit;
        /**
         * 进场时间(yyyy-MM-dd HH:mm:ss)
         */
        @JsonProperty("EntranceDate")
        private String entranceDate;
        /**
         * 进场通道ID
         */
        @JsonProperty("EntranceGateID")
        private String entranceGateID;
        /**
         * 进场通道名称
         */
        @JsonProperty("EntranceGateName")
        private String entranceGateName;
        /**
         * 出场时间(yyyy-MM-dd HH:mm:ss)，未出场时，为空字符串
         */
        @JsonProperty("ExitDate")
        private String exitDate;
        /**
         * 出场通道ID
         */
        @JsonProperty("ExitGateID")
        private String exitGateID;
        /**
         * 出场通道名称
         */
        @JsonProperty("ExitGateName")
        private String exitGateName;
        /**
         * 0 正常放行 1 收费放行 2 免费放行 3 异常放行
         */
        @JsonProperty("ReleaseType")
        private Integer releaseType;
        /**
         * 进出备注信息
         */
        @JsonProperty("Remark")
        private String remark;
    }
}
