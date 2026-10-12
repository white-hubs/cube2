package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SetTallyDataResponse {

    /**
     * 客户端唯一
     */
    private String guid;

    /**
     * 理货机编码
     */
    private String deviceId;

    /**
     * 批次id
     */
    private String tallyId;

    /**
     * 理货状态
     */
    private Integer eventType;

    /**
     * 理货开始时间
     */
    private LocalDateTime startTime;

    /**
     * 理货结束时间
     */
    private LocalDateTime endTime;

    /**
     * 理货详细数据
     */
    private List<TallyDetailsResponse> datas;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class TallyDetailsResponse {

        /**
         * 挂钩号
         */
        private String rfid1;

        /**
         * 脚标号
         */
        private String rfid2;

        /**
         * 重量
         */
        private BigDecimal weight;

        /**
         * 理货时间
         */
        private LocalDateTime time;

        /**
         * 图片1
         */
        private String picPath1;

        /**
         * 图片2
         */
        private String picPath2;
    }
}
