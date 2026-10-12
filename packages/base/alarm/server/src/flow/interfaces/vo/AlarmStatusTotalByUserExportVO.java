package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 按用户分组统计报警状态导出返回参数
 *
 * @author lanchun
 * @date 2022/11/9 9:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class AlarmStatusTotalByUserExportVO {

    /**
     * 用户名称
     */
    @ExcelProperty(value = { "姓名" }, index = 0)
    private String userName;

    /**
     * 部门名称
     */
    @ExcelProperty(value = { "部门" }, index = 1)
    private String departmentName;

    /**
     * 部门报警数
     */
    @ExcelProperty(value = { "部门报警数" }, index = 2)
    private Long departmentTotalNum;

    /**
     * 负责的报警数
     */
    @ExcelProperty(value = { "个人报警数" }, index = 3)
    private Long totalNum;

    /**
     * 未响应报警数
     */
    @ExcelProperty(value = { "未响应报警数" }, index = 4)
    private Long unconfirmedNum;

    /**
     * 处理中报警数
     */
    @ExcelProperty(value = { "处理中报警数" }, index = 5)
    private Long progressConfirmedNum;

    /**
     * 已处理报警数
     */
    @ExcelProperty(value = { "已处理报警数" }, index = 6)
    private Long handlerNum;

    /**
     * 已挂起报警数
     */
    @ExcelProperty(value = { "已挂起报警数" }, index = 7)
    private Long suspendNum;

    /**
     * 超时数量
     */
    @ExcelProperty(value = { "超时报警数" }, index = 8)
    private Long timeoutNum;

    /**
     * 报警占比
     */
    @ExcelProperty(value = { "报警占比" }, index = 9)
    private String ratio;

    /**
     * 处理完成率
     */
    @ExcelProperty(value = { "报警完成率" }, index = 10)
    private String handlerRate;

    /**
     * 报警响应率
     */
    @ExcelProperty(value = { "报警响应率" }, index = 11)
    private String confirmedRate;

    /**
     * 报警超时率
     */
    @ExcelProperty(value = { "报警超时率" }, index = 12)
    private String timeoutRate;

    /**
     * 处理耗时(分钟)
     */
    private Long processingTime;

    /**
     * 平均处理耗时
     */
    @ExcelProperty(value = { "处理平均耗时" }, index = 13)
    private String averageProcessing;

    public String getAverageProcessing() {
        if (Objects.nonNull(this.handlerNum) && 0 < this.handlerNum) {
            //计算耗时
            //平均耗时秒数
            long averageSeconds = new BigDecimal(this.processingTime)
                    .divide(new BigDecimal(this.getHandlerNum()), BigDecimal.ROUND_UP).longValue();
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
