package com.guhecloud.cube.base.device.center.statistics.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 设备维护统计
 *
 * @author lanchun
 * @date 2025/4/15 15:32
 */
@Data
public class DeviceMaintainStatisticsVO {

    /**
     * 设备id
     */
    private Long id;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 所属产品
     */
    private String productName;
    /**
     * 产品品类
     */
    private String productCategoryName;

    /**
     * 区域信息
     */
    private AreaBaseInfoDTO area;

    /**
     * 隐患单统计
     */
    private MaintainCommonStatistics hiddenDanger;

    /**
     * 故障单统计
     */
    private MaintainCommonStatistics fault;

    /**
     * 巡检统计
     */
    private InspectStatistics inspect;

    /**
     * 维保统计
     */
    private UpkeepStatistics upkeep;

    public MaintainCommonStatistics getHiddenDanger() {
        if (Objects.isNull(this.hiddenDanger)) {
            return new MaintainCommonStatistics();
        }
        return hiddenDanger;
    }

    public MaintainCommonStatistics getFault() {
        if (Objects.isNull(this.fault)) {
            return new MaintainCommonStatistics();
        }
        return fault;
    }

    public InspectStatistics getInspect() {
        if (Objects.isNull(this.inspect)) {
            return new InspectStatistics();
        }
        return inspect;
    }

    public UpkeepStatistics getUpkeep() {
        if (Objects.isNull(this.upkeep)) {
            return new UpkeepStatistics();
        }
        return upkeep;
    }

    @Data
    public static class MaintainCommonStatistics {

        /**
         * 总数
         */
        private Integer num;

        /**
         * 已处理数
         */
        private Integer handlerNum;

        /**
         * 未处理数
         */
        private Integer unHandlerNum;

        /**
         * 处理率
         */
        @Decimal
        private BigDecimal handlerRatio;
    }

    /**
     * 巡检统计
     */
    @Data
    public static class InspectStatistics {

        /**
         * 工单数量
         */
        private Integer num;

        /**
         * 任务数量
         */
        private Integer taskNum;

        /**
         * 异常数量
         */
        private Integer abnormalNum;

        /**
         * 异常率
         */
        @Decimal
        private BigDecimal abnormalRatio;
    }

    /**
     * 维保统计
     */
    @Data
    public static class UpkeepStatistics {

        /**
         * 数量
         */
        private Integer num;

        /**
         * 最近时间
         */
        private LocalDateTime lastTime;
    }
}
