package com.guhecloud.cube.biz.adapter.park.server.facade.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 车场信息返回参数
 *
 * @author lanchun
 * @date 2022/10/8 16:21
 */
@Data
public class ParkingInfoResponse {

    /**
     * 总车位
     */
    @JsonProperty("totalnum")
    private Integer totalNum;

    /**
     * 固定车位
     */
    @JsonProperty("fixnum")
    private Integer fixNum;

    /**
     * 非固定车位
     */
    @JsonProperty("tempnum")
    private Integer tempNum;

    /**
     * 剩余总车位
     */
    @JsonProperty("totalleftnum")
    private Integer totalLeftNum;

    /**
     * 剩余固定车位
     */
    @JsonProperty("fixleftnum")
    private Integer fixLeftNum;

    /**
     * 剩余非固定车位
     */
    @JsonProperty("templeftnum")
    private Integer tempLeftNum;

    /**
     * 每个子停车场的详细情况
     */
    private List<ParkingInfosResponse> parks;

    @Data
    public static class ParkingInfosResponse {

        /**
         * 停车场编号
         */
        private String no;

        /**
         * 停车场简称
         */
        private String name;

        /**
         * 总车位
         */
        @JsonProperty("total")
        private Integer totalNum;

        /**
         * 固定车位
         */
        @JsonProperty("fix")
        private Integer fixNum;

        /**
         * 非固定车位
         */
        @JsonProperty("temp")
        private Integer tempNum;

        /**
         * 剩余总车位
         */
        @JsonProperty("total_2")
        private Integer totalLeftNum;

        /**
         * 剩余固定车位
         */
        @JsonProperty("fix_2")
        private Integer fixLeftNum;

        /**
         * 剩余非固定车位
         */
        @JsonProperty("temp_2")
        private Integer tempLeftNum;
    }
}
