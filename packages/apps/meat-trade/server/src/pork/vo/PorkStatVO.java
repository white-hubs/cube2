package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 猪肉滑轨数据看板信息
 *
 * @author lzy
 * @date 2021/11/23
 */
@Data
public class PorkStatVO {

    /**
     * 食安抽检信息
     */
    @Data
    public static class PorkStatSpotCheck {
        /**
         * 供货商报备总数
         */
        private Integer supplyReports;

        /**
         * 检疫票不合格主键
         */
        private List<Long> pratiqueFailIds;

        /**
         * 检疫票不合格数量
         */
        private Integer pratiqueFailed;

        /**
         * 检疫票不合格百分比
         */
        @Decimal
        private BigDecimal pratiqueFailedPer;

        /**
         * 检疫票不合格环比
         */
        @Decimal
        private BigDecimal qoqPratiqueFailed;

        /**
         * 瘦肉精不合格
         */
        private Integer clenbuterolFailed;

        /**
         * 瘦肉精不合格百分比
         */
        @Decimal
        private BigDecimal clenbuterolFailedPer;

        /**
         * 瘦肉精不合格环比
         */
        @Decimal
        private BigDecimal qoqClenbuterolFailed;

        /**
         * 含水量不合格
         */
        private Integer watered;

        /**
         * 含水量不合格百分比
         */
        @Decimal
        private BigDecimal wateredPer;

        /**
         * 含水量不合格环比
         */
        @Decimal
        private BigDecimal qoqWatered;
    }

    /**
     * 理货数据审核
     */
    @Data
    public static class PorkStatArrangeAudit {
        /**
         * 理货数据审核总数
         */
        private Integer total;

        /**
         * 理货审核合格率
         */
        @Decimal
        private BigDecimal porkArrangeAuditPassed;

        /**
         * 理货审核合格率环比
         */
        @Decimal
        private BigDecimal qoqPorkArrangeAuditPassed;

        /**
         * 理货合格数
         */
        private Integer porkArrangeAuditPassedNum;

        /**
         * 理货不合格率
         */
        @Decimal
        private BigDecimal porkArrangeAuditFailed;

        /**
         * 理货不合格率环比
         */
        @Decimal
        private BigDecimal qoqPorkArrangeAuditFailed;

        /**
         * 理货不合格数
         */
        private Integer porkArrangeAuditFailedNum;
    }

    /**
     * 来货报备统计信息
     */
    @Data
    public static class PorkStatReport {
        /**
         * 来货报备总数
         */
        private Integer total;

        /**
         * 来货报备通过率
         */
        @Decimal
        private BigDecimal porkReportPassed;

        /**
         * 来货报备通过数
         */
        private Integer porkReportPassedNum;

        /**
         * 来货报备不通过率
         */
        @Decimal
        private BigDecimal porkReportFailed;

        /**
         * 来货报备不通过数
         */
        private Integer porkReportFailedNum;
    }

    /**
     * 报备商家统计信息
     */
    @Data
    public static class PorkStatRiskLevel {

        /**
         * 总数
         */
        private Integer total;

        /**
         * 低风险商家
         */
        @Decimal
        private BigDecimal lowRiskLevel;

        /**
         * 低风险商家数
         */
        private Integer lowRiskLevelNum;

        /**
         * 中风险商家
         */
        @Decimal
        private BigDecimal mediumRiskLevel;

        /**
         * 中风险商家数
         */
        private Integer mediumRiskLevelNum;

        /**
         * 高风险商家
         */
        @Decimal
        private BigDecimal highRiskLevel;

        /**
         * 高风险商家数
         */
        private Integer highRiskLevelNum;
    }

    /**
     * 批次嫌疑信息
     */
    @Data
    public static class ReportSuspicion {

        /**
         * 总数
         */
        private Integer total;

        /**
         * 有嫌疑率
         */
        @Decimal
        private BigDecimal suspicion;

        /**
         * 有嫌疑环比
         */
        @Decimal
        private BigDecimal qoqSuspicion;

        /**
         * 有嫌疑数
         */
        private Integer suspicionNum;

        /**
         * 无嫌疑率
         */
        @Decimal
        private BigDecimal noDoubt;

        /**
         * 无嫌疑数
         */
        private Integer noDoubtNum;

        /**
         * 无嫌疑环比
         */
        @Decimal
        private BigDecimal qoqNoDoubt;
    }

    /**
     * 理货明细统计
     */
    @Data
    public static class PorkStatArrange {

        /**
         * 序号
         */
        private Integer sort;

        /**
         * 理货机名称
         */
        private String machineName;

        /**
         * 理货批次
         */
        private String porkReportCode;

        /**
         * 商家名称
         */
        private String supplierName;

        /**
         * 商家编码
         */
        private String supplierCode;

        /**
         * 理货片数
         */
        private Integer porkArrangeSlices;

        /**
         * 理货结果
         */
        private Boolean result;
    }

    /**
     * 白条出场情况
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class PorkStatAbattoir extends PorkStatSummaryVO {
        /**
         * 屠宰场名称
         */
        private String abattoirName;

        /**
         * 排序
         */
        private Integer sort;
    }

    /**
     * 折线图数据
     */
    @Data
    public static class PorkStatLineVO {
        /**
         * 日期
         */
        private LocalDateTime day;

        /**
         * 数值
         */
        @Decimal
        private BigDecimal value;
    }
}
