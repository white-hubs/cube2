package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * 按用户分组统计报警状态的分页返回参数
 *
 * @author lanchun
 * @date 2022/11/9 9:31
 */
@Data
public class AlarmStatusTotalByUserPageVO {

    /**
     * 报警事件类型
     */
    private String alarmEventName;

    /**
     * 报警事件类型
     */
    private String alarmEventTypeId;
    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 部门主键
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门报警数
     */
    private Long departmentTotalNum;
    /**
     * 报警总数
     */
    private Long alarmTotalNum;

    /**
     * 负责的报警数
     */
    private Long totalNum;

    /**
     * 未响应报警数
     */
    private Long unconfirmedNum;

    /**
     * 处理中报警数
     */
    private Long progressConfirmedNum;

    /**
     * 已挂起报警数
     */
    private Long suspendNum;

    /**
     * 已处理报警数
     */
    private Long handlerNum;

    /**
     * 超时数量
     */
    private Long timeoutNum;

    /**
     * 真实报警数
     */
    private Long alarmActualNum;

    /**
     * 误报数
     */
    private Long alarmImpracticalNum;

    /**
     * 报警占比
     */
    private String ratio;

    /**
     * 报警正确率
     */
    private String correctRate;

    /**
     * 报警响应率
     */
    private String confirmedRate;

    /**
     * 报警超时率
     */
    private String timeoutRate;

    /**
     * 处理耗时(分钟)
     */
    private Long processingTime;

    /**
     * 平均处理耗时
     */
    private String averageProcessing;

    /**
     * 处理完成率
     */
    private String handlerRate;

    public String getRatio() {
        return getBigDecimalFormatString(departmentTotalNum, totalNum);
    }

    public String getConfirmedRate() {
        return getBigDecimalFormatString(totalNum, totalNum - unconfirmedNum);
    }

    public String getTimeoutRate() {
        return getBigDecimalFormatString(totalNum, timeoutNum);
    }

    public String getHandlerRate() {
        return getBigDecimalFormatString(totalNum, handlerNum);
    }

    public String getCorrectRate() {
        return getBigDecimalFormatString(handlerNum, alarmActualNum);
    }

    private String getBigDecimalFormatString(Long totalNum, Long radixNum) {
        radixNum = Objects.nonNull(radixNum) ? radixNum : 0L;
        if (Objects.nonNull(totalNum) && 0 < totalNum) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(new BigDecimal(radixNum)
                                                .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP)
                                                .multiply(new BigDecimal(100)).doubleValue()) + "%";
        }
        return "0.00%";
    }
}
