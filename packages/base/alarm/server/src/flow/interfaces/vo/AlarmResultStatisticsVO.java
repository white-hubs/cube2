package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import lombok.Data;

/**
 * 报警处理结果统计返回参数
 *
 * @author lanchun
 * @date 2022/6/7 14:13
 */
//@Builder
@Data
public class AlarmResultStatisticsVO {

    /**
     * 处理结果
     */
    private AlarmHandlerResultEnum alarmHandlerResult;

    /**
     * 处理结果名称
     */
    private String alarmHandlerResultName;

    /**
     * 数量
     */
    private Long alarmNum;

}
