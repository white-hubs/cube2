package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 报警报表
 *
 * @author lanchun
 * @date 2023/9/8 14:58
 */
@Data
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmReportPageVO {

    private Integer marketTenant;

    /**
     * 市场名称
     */
    @ExcelProperty(value = { "市场名称" }, index = 0)
    private String marketName;

    /**
     * 报警总数
     */
    @ExcelProperty(value = { "报警总数" }, index = 1)
    private Integer totalNum;

    /**
     * 未响应报警数
     */
    @ExcelProperty(value = { "未响应报警数" }, index = 2)
    private Integer unConfirmedNum;

    /**
     * 处理中报警数
     */
    @ExcelProperty(value = { "处理中报警数" }, index = 3)
    private Integer progressConfirmedNum;

    /**
     * 已处理报警数
     */
    @ExcelProperty(value = { "已处理报警数" }, index = 4)
    private Integer handlerNum;

    /**
     * 已挂起报警数
     */
    @ExcelProperty(value = { "已挂起报警数" }, index = 5)
    private Integer suspendNum;

    /**
     * 超时数量数
     */
    @ExcelProperty(value = { "超时数量数" }, index = 6)
    private Integer timeoutNum;

    /**
     * 真实报警数
     */
    @ExcelProperty(value = { "真实报警数" }, index = 7)
    private Integer actualNum;

    /**
     * 误报数
     */
    @ExcelProperty(value = { "误报数" }, index = 8)
    private Integer impracticalNum;

    /**
     * 报警正确率(处理结果为真实报警的报警数/已处理的报警数)
     */
    @ExcelProperty(value = { "报警正确率" }, index = 9)
    private BigDecimal actualRatio;

    /**
     * 报警处理率(状态为“已处理”的报警数/报警总数)
     */
    @ExcelProperty(value = { "报警处理率" }, index = 10)
    private BigDecimal handlerRatio;

    /**
     * 报警响应率(已响应报警数（除待确认状态外）/报警总数)
     */
    @ExcelProperty(value = { "报警响应率" }, index = 11)
    private BigDecimal confirmedRatio;

    /**
     * 报警超时率(已超时的报警数/报警总数)
     */
    @ExcelProperty(value = { "报警超时率" }, index = 12)
    private BigDecimal timeOutRatio;

    /**
     * 处理耗时(分钟)
     */
    private Long processingTime;

    /**
     * 平均处理耗时
     */
    @ExcelProperty(value = { "平均处理耗时" }, index = 13)
    private String averageProcessing;

    public BigDecimal getActualRatio() {
        if (0 == this.handlerNum) {
            return BigDecimal.valueOf(0.0);
        }
        return BigDecimal.valueOf(this.actualNum)
                .divide(BigDecimal.valueOf(this.handlerNum), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public BigDecimal getHandlerRatio() {
        if (0 == this.totalNum) {
            return BigDecimal.valueOf(0.0);
        }
        return BigDecimal.valueOf(this.handlerNum)
                .divide(BigDecimal.valueOf(this.totalNum), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public BigDecimal getConfirmedRatio() {
        if (0 == this.totalNum) {
            return BigDecimal.valueOf(0.0);
        }
        return BigDecimal.valueOf(this.handlerNum + this.progressConfirmedNum + this.suspendNum)
                .divide(BigDecimal.valueOf(this.totalNum), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public BigDecimal getTimeOutRatio() {
        if (0 == this.totalNum) {
            return BigDecimal.valueOf(0.0);
        }
        return BigDecimal.valueOf(this.timeoutNum)
                .divide(BigDecimal.valueOf(this.totalNum), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public String getAverageProcessing() {
        if (Objects.nonNull(this.handlerNum) && 0 < this.handlerNum) {
            //计算耗时
            //平均耗时秒数
            long averageSeconds = new BigDecimal(this.processingTime)
                    .divide(new BigDecimal(this.handlerNum), BigDecimal.ROUND_UP).longValue();
            //分钟数转为秒数
            this.averageProcessing = this.dateConvertRemark(averageSeconds);
        } else {
            this.averageProcessing = this.dateConvertRemark(0);
        }
        return averageProcessing;
    }

    private String dateConvertRemark(long seconds) {
        long nd = 24L * 60L * 60L;
        long nh = 60L * 60L;
        long nm = 60;
        // 计算差多少小时
        long hour = seconds % nd / nh;
        // 计算差多少分钟
        long min = seconds % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = seconds % nd % nh % nm;
        return hour + "小时" + min + "分" + sec + "秒";
    }
}
